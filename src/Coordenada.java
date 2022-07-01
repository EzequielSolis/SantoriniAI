import java.util.ArrayList;

public class Coordenada {
    private int x;
    private int y;

    Coordenada(int a, int b){
        this.x = a;
        this.y = b;
    }

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Coordenada)) {
            return false;
        }
        Coordenada c = (Coordenada) o;
        if (c.getX() == this.getX() && c.getY() == this.getY())
            return true;
        return false;
    }

        ArrayList<Coordenada> adyacentes(){
        ArrayList<Coordenada> ret = new ArrayList<Coordenada>();

        for(int a = 0; a < 5; a++){
            for(int b = 0; b < 5; b++){
                if(a == x && (b == y + 1 || b == y -1)){
                    ret.add(new Coordenada(a,b));
                } else if(b == y && (a == x + 1 || a == x -1)){
                    ret.add(new Coordenada(a,b));
                } else if((a == x + 1 || a == x -1) && (b == y + 1 || b == y -1)){ //diagonales
                    ret.add(new Coordenada(a,b));
                }
            }
        }

        return ret;
    }

    String mostrarCoordeenada(){
        return String.format("%s%s", x, y);
    }

    String mostrarAdyacentes(){
        String ret = "";
        for (Coordenada c : this.adyacentes()) {
            ret += "\n" + c.mostrarCoordeenada();
        }
        return ret;
    }


}
