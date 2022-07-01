import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Estado implements Comparable<Estado> {
    private int tablero[][]= new int[5][5];
    private Coordenada posMaxA;
    private Coordenada posMaxB;
    private Coordenada posMinA;
    private Coordenada posMinB;
    public short puntuacionEstimada = 0;

    Estado(Coordenada a, Coordenada b, Coordenada c, Coordenada d){
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                tablero[i][j] = 0;
            }
        }
        posMaxA = a;
        posMaxB = b;
        posMinA = c;
        posMinB = d;
    }

    Estado(int[][] tab, Coordenada a, Coordenada b, Coordenada c, Coordenada d) {
        for(int i = 0; i < 5; i++){
            System.arraycopy(tab[i], 0, this.tablero[i], 0, 5);
        }
        posMaxA = a;
        posMaxB = b;
        posMinA = c;
        posMinB = d;
    }

    Estado(int[][] tab, Coordenada a, Coordenada b, Coordenada c, Coordenada d, Coordenada construida, boolean jugador, boolean sube){
        for(int i = 0; i < 5; i++){
            System.arraycopy(tab[i], 0, this.tablero[i], 0, 5);
        }

        tablero[construida.getX()][construida.getY()]++;



        if (jugador){
            if(tablero[a.getX()][a.getY()] == 3 || tablero[b.getX()][b.getY()] == 3)
                puntuacionEstimada = 50; //puntuación máxima
            else{
                if(sube) puntuacionEstimada+= 4;
                puntuacionEstimada += tablero[a.getX()][a.getY()] * 10;
                if(a.getY() == 2)
                    puntuacionEstimada += 5;
                else if(a.getY() == 1 || a.getY() == 3)
                    puntuacionEstimada += 3;

                if(a.getX() == 2)
                    puntuacionEstimada += 5;
                else if(a.getX() == 1 || a.getX() == 3)
                    puntuacionEstimada += 3;

                puntuacionEstimada += tablero[b.getX()][b.getY()] * 10;
                if(b.getY() == 2)
                    puntuacionEstimada += 5;
                else if(b.getY() == 1 || b.getY() == 3)
                    puntuacionEstimada += 3;

                if(b.getX() == 2)
                    puntuacionEstimada += 5;
                else if(b.getX() == 1 || b.getX() == 3)
                    puntuacionEstimada += 3;
            }
        } else {
            if(tablero[c.getX()][c.getY()] == 3 || tablero[c.getX()][c.getY()] == 3)
                puntuacionEstimada = 50; //puntuación máxima
            else{
                if(sube) puntuacionEstimada+= 4;
                puntuacionEstimada += tablero[c.getX()][c.getY()] * 10;
                if(c.getY() == 2)
                    puntuacionEstimada += 5;
                else if(c.getY() == 1 || c.getY() == 3)
                    puntuacionEstimada += 3;

                if(c.getX() == 2)
                    puntuacionEstimada += 5;
                else if(c.getX() == 1 || c.getX() == 3)
                    puntuacionEstimada += 3;

                if(sube) puntuacionEstimada+= 5;
                puntuacionEstimada += tablero[d.getX()][d.getY()] * 10;
                if(d.getY() == 2)
                    puntuacionEstimada += 5;
                else if(d.getY() == 1 || d.getY() == 3)
                    puntuacionEstimada += 3;

                if(d.getX() == 2)
                    puntuacionEstimada += 5;
                else if(d.getX() == 1 || d.getX() == 3)
                    puntuacionEstimada += 3;
            }
        }



        posMaxA = a;
        posMaxB = b;
        posMinA = c;
        posMinB = d;
    }

    Estado(Estado e, Coordenada a, Coordenada b, Coordenada c, Coordenada d, Coordenada construida){
        for(int i = 0; i < 5; i++){
            System.arraycopy(e.tablero[i], 0, this.tablero[i], 0, 5);
        }


        tablero[construida.getX()][construida.getY()]++;

        posMaxA = a;
        posMaxB = b;
        posMinA = c;
        posMinB = d;
    }

    Estado(Estado e) {
        for(int i = 0; i < 5; i++){
            System.arraycopy(e.tablero[i], 0, this.tablero[i], 0, 5);
        }

        this.posMaxA = e.posMaxA;
        this.posMaxB = e.posMaxB;
        this.posMinA = e.posMinA;
        this.posMinB = e.posMinB;

    }

    Coordenada getA(){
        return this.posMaxA;
    }

    Coordenada getB(){
        return this.posMaxB;
    }

    Coordenada getC(){
        return this.posMinA;
    }

    Coordenada getD(){
        return this.posMinB;
    }




    int evaluacion(){
        int puntos = 0;
        int puntosA = 0;
        int puntosB = 0;
        int puntosC = 0;
        int puntosD = 0;

        boolean atascadoA = true;
        boolean atascadoB = true;
        boolean atascadoC = true;
        boolean atascadoD = true;

        for (Coordenada c : posMaxA.adyacentes() ) {
            if (this.movimientoLegal(posMaxA, c)) {
                puntosA += puntuacionAdyacente(this.tablero[c.getX()][c.getY()]);
                atascadoA = false;
            }
        }

        for (Coordenada c : posMaxB.adyacentes() ) {
            if (this.movimientoLegal(posMaxB, c)) {
                puntosB += puntuacionAdyacente(this.tablero[c.getX()][c.getY()]);
                atascadoB = false;
            }
        }
        if (atascadoA && atascadoB)
            return -10000; //derrota

        for (Coordenada c : posMinA.adyacentes() ) {
            if (this.movimientoLegal(posMinA, c)) {
                puntosC -= puntuacionAdyacente(this.tablero[c.getX()][c.getY()]);
                atascadoC = false;
            }
        }
        for (Coordenada c : posMinB.adyacentes() ) {
            if (this.movimientoLegal(posMinB, c)) {
                puntosD -= puntuacionAdyacente(this.tablero[c.getX()][c.getY()]);
                atascadoD = false;
            }
        }

        if (atascadoC && atascadoD)
            return 10000; //victoria

        if(this.tablero[posMaxA.getX()][posMaxA.getY()] == 3){
            return 10000;
        } else if(this.tablero[posMaxB.getX()][posMaxB.getY()] == 3){
            return 10000;
        } else if(this.tablero[posMinA.getX()][posMinA.getY()] == 3){
            return -10000;
        } else if(this.tablero[posMinB.getX()][posMinB.getY()] == 3){
            return -10000;
        } else{
            puntos += this.tablero[posMaxA.getX()][posMaxA.getY()] * 100;
            puntos += this.tablero[posMaxB.getX()][posMaxB.getY()] * 100;
            puntos -= this.tablero[posMinA.getX()][posMinA.getY()] * 100;
            puntos -= this.tablero[posMinB.getX()][posMinB.getY()] * 100;
        }

        if(atascadoA || atascadoB)
            puntos -= 300;

        if(atascadoC || atascadoD){
            puntos += 300;
        }

        return puntos + puntosA + puntosB + puntosC + puntosD;
    }


    ArrayList<Estado> hijos(boolean jugadorMax){
        ArrayList<Estado> ret = new ArrayList<Estado>();

        if(jugadorMax){
            for (Coordenada c : posMaxA.adyacentes() ) {
                if(this.movimientoLegal(posMaxA, c)){
                    for (Coordenada d: c.adyacentes()){
                        if(this.construccionLegal(posMaxA, d)){
                            ret.add(new Estado(this.tablero, c, posMaxB, posMinA, posMinB, d, true, this.tablero[c.getX()][c.getY()] > this.tablero[posMaxA.getX()][posMaxA.getX()]));
                        }
                    }
                }
            }

            for (Coordenada c : posMaxB.adyacentes() ) {
                if(this.movimientoLegal(posMaxB, c)){
                    for (Coordenada d: c.adyacentes()){
                        if(this.construccionLegal(posMaxB, d)){
                            ret.add(new Estado(this.tablero, posMaxA, c, posMinA, posMinB, d, true, this.tablero[c.getX()][c.getY()] > this.tablero[posMaxB.getX()][posMaxB.getX()]));
                        }
                    }
                }
            }
        } else {
            for (Coordenada c : posMinA.adyacentes() ) {
                if(this.movimientoLegal(posMinA, c)){
                    for (Coordenada d: c.adyacentes()){
                        if(this.construccionLegal(posMinA, d)){
                            ret.add(new Estado(this.tablero, posMaxA, posMaxB, c, posMinB, d, false, this.tablero[c.getX()][c.getY()] > this.tablero[posMinA.getX()][posMinA.getX()]));
                        }
                    }
                }
            }

            for (Coordenada c : posMinB.adyacentes() ) {
                if(this.movimientoLegal(posMinB, c)){
                    for (Coordenada d: c.adyacentes()){
                        if(this.construccionLegal(posMinB, d)){
                            ret.add(new Estado(this.tablero, posMaxA, posMaxB, posMinA, c, d, false, this.tablero[c.getX()][c.getY()] > this.tablero[posMinB.getX()][posMinB.getX()]));
                        }
                    }
                }
            }
        }
        Collections.sort(ret);

        return ret;
    }

    boolean movimientoLegal(Coordenada org, Coordenada dest){
        if (this.tablero[dest.getX()][dest.getY()] <= this.tablero[org.getX()][org.getY()] + 1 && !casillaOcupada(dest) )
            return true;
        else
            return false;
    }

    boolean casillaOcupada(Coordenada dest){
        if (dest.equals(posMaxA) || dest.equals(posMaxB) || dest.equals(posMinA) || dest.equals(posMinB))
            return true;
        else
            return false;
    }

    boolean construccionLegal(Coordenada org, Coordenada dest){
        //se añade la casilla origen, ya que movimientoLegal solo compara con los personajes estáticos, para cuando se llega aquí, el personaje ha dejado un hueco aún no representado
        if (tablero[dest.getX()][dest.getY()] < 4 && (dest.equals(org) || !casillaOcupada(dest)) )
            return true;
        else
            return false;
    }

    String mostrarEstado(){
        StringBuilder ret = new StringBuilder();
        ret.append("------------------------------\n");
        for(int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(posMaxA.equals(new Coordenada(i,j))){
                    ret.append(String.format("%s%s   ", tablero[i][j], "A"));
                } else if(posMaxB.equals(new Coordenada(i,j))){
                    ret.append(String.format("%s%s   ", tablero[i][j], "B"));
                } else if(posMinA.equals(new Coordenada(i,j))){
                    ret.append(String.format("%s%s   ", tablero[i][j], "C"));
                } else if(posMinB.equals(new Coordenada(i,j))){
                    ret.append(String.format("%s%s   ", tablero[i][j], "D"));
                } else{
                    ret.append(String.format("%s    ", tablero[i][j]));
                }
            }
            ret.append("\n");
        }
        ret.append("------------------------------");
        return ret.toString();
    }

    int puntuacionAdyacente(int valorTerreno){
        //return (int) Math.pow(3, (1 + valorTerreno));
        return 5 * (++valorTerreno);
    }

    @Override
    public int compareTo(Estado o) {
        return o.puntuacionEstimada - this.puntuacionEstimada;
    }
}
