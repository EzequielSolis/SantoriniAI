import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.*;

/*
input example: d28 (d moves down and builds up)
 */
public class main {

    private static Estado resultado;

    public static void main(String[] args) throws IOException {
        jugar();
        //test();
    }


    static void jugar()throws IOException {

        Coordenada a = new Coordenada(1, 1);
        Coordenada b = new Coordenada(3, 3);
        Coordenada c = new Coordenada(3, 1);
        Coordenada d = new Coordenada(1, 2);

        int[][] tab = {{0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
        };

        Estado e = new Estado(tab, a, b, c, d);
        ArrayList<Estado> jugadas = new ArrayList<>();

        jugadas.add(e);

        System.out.println("----------------------->>>>>>>>>>>>>>>>>>>>>>>>>");
        int profundidad = 5;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        //TODO: ya hay historia, crear un comando de "atras"
        while(true){
            System.out.println(e.mostrarEstado());
            System.out.println("Jugador que se mueve? a b c d:");
            String inl = br.readLine();
            String jugador = inl.substring(0,1);
            String pattern = "^([ab]|[cd][12346789][12346789][+-]?)$";

            if(!Pattern.matches(pattern, inl)){
                System.out.println("Error en el input");
            } else {
                if (jugador.equals("c") || jugador.equals("d")) {
                    String mov = inl.substring(1,2);
                    String cons = inl.substring(2,3);
                    if(inl.length() == 4){
                        System.out.println(inl.substring(3,4) );
                        if(inl.substring(3,4).equals("+"))
                            profundidad++;
                        else
                            profundidad--;
                    }
                    int x, y;
                    if (jugador.equals("c")){
                        x = e.getC().getX();
                        y = e.getC().getY();
                    } else{
                        x = e.getD().getX();
                        y = e.getD().getY();
                    }

                    Coordenada n = null;
                    switch(mov){
                        case "7": n = new Coordenada(x-1, y-1); break;
                        case "8": n = new Coordenada(x-1, y); break;
                        case "9": n = new Coordenada(x-1, y+1); break;
                        case "4": n = new Coordenada(x, y-1); break;
                        case "6": n = new Coordenada(x, y+1); break;
                        case "1": n = new Coordenada(x+1, y-1); break;
                        case "2": n = new Coordenada(x+1, y); break;
                        case "3": n = new Coordenada(x+1, y+1); break;
                    }

                    Coordenada cordCons = null;

                    x = n.getX();
                    y = n.getY();

                    switch(cons){
                        case "7": cordCons = new Coordenada(x-1, y-1); break;
                        case "8": cordCons = new Coordenada(x-1, y); break;
                        case "9": cordCons = new Coordenada(x-1, y+1); break;
                        case "4": cordCons = new Coordenada(x, y-1); break;
                        case "6": cordCons = new Coordenada(x, y+1); break;
                        case "1": cordCons = new Coordenada(x+1, y-1); break;
                        case "2": cordCons = new Coordenada(x+1, y); break;
                        case "3": cordCons = new Coordenada(x+1, y+1); break;
                    }


                    if (jugador.equals("c")){
                        e = new Estado(e, e.getA(), e.getB(), n, e.getD(), cordCons);
                    } else {
                        e = new Estado(e, e.getA(), e.getB(), e.getC(), n, cordCons);
                    }
                    System.out.println(e.mostrarEstado());
                    jugadas.add(e);
                    System.out.println("Procesando turno...");
                    System.out.println("Profundidad: " + profundidad);
                    long startTime = System.nanoTime();
                    System.out.println(minmax(e, profundidad, true, profundidad, -9999999, 9999999));
                    long endTime = System.nanoTime();
                    long timeElapsed = endTime - startTime;
                    System.out.println("Execution time in milliseconds : " +
                            timeElapsed / 1000000);
                    e = new Estado(resultado);
                    jugadas.add(e);

                } else{
                    System.out.println(minmax(e, profundidad, true, profundidad, -9999999, 999999));
                    e = new Estado(resultado);
                    jugadas.add(e);
                }
            }
        }

    }

    private static int minmax(Estado e, int profundidad, boolean jugadorMax, int profundidadInicial, int alpha, int beta) {
        int evaluacion = e.evaluacion();
        if( evaluacion >= 10000 || evaluacion <= -10000 ||profundidad == 0){
            if(evaluacion >= 10000)
                return evaluacion + profundidad;
            else if(evaluacion <= -10000)
                return evaluacion - profundidad;
            else
                return evaluacion;
        }

        if(jugadorMax){
            int maxEva = -9999999;
            for (Estado o: e.hijos(true)) {

                int eva = minmax(o, profundidad - 1, false, profundidadInicial, alpha, beta);
                //maxEva = Math.max(maxEva, eva);
                if(eva > maxEva){
                    maxEva = eva;
                    if(profundidad == profundidadInicial){
                        resultado = new Estado(o);
                    }
                }
                alpha = Math.max(alpha, eva);
                if (beta <=alpha)
                    break;

            }

            return maxEva;

        } else {
            int minEva = 999999;
            for (Estado o: e.hijos(false)) {
                int eva = minmax(o, profundidad - 1, true, profundidadInicial, alpha, beta);
                minEva = Math.min(minEva, eva);
                beta = Math.min(beta, eva);
                if(beta <= alpha)
                    break;
            }

            return minEva;
        }
    }

/*

profundidad= 4

https://www.techiedelight.com/efficiently-sort-array-duplicated-values/

alpha/beta:         165 ms
ordenado simple (solo victoria) 889??? (muchos números iguales)
ordenado un poco mas complejo   161 ms
ordenado mas complejo pero sin construida: 113 (ACTUAL METODO)
sin ordenar:        878 ms
2 jugadores menos:  148 ms
sin ordenar:        448 ms

 */
    static void test(){
        int[][] tab = {{0, 0, 0, 0, 1},
                {0, 0, 0, 1, 1},
                {0, 0, 0, 1, 0},
                {0, 0, 2, 0, 1},
                {0, 0, 0, 2, 2},
        };

        Coordenada a = new Coordenada(2, 2);
        Coordenada b = new Coordenada(3, 3);
        Coordenada c = new Coordenada(2, 1);
        Coordenada d = new Coordenada(2, 3);

        Estado e = new Estado(tab, a, b, c, d);
        int profundidad = 5;
        int iteraciones = 50;

        long startTime = System.nanoTime();
        for(int i = 0; i < iteraciones; i++){
            minmax(e, profundidad, true, profundidad, -9999999, 9999999);
        }
        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("Execution time in milliseconds : " + (timeElapsed / 1000000) / iteraciones);
    }
}
