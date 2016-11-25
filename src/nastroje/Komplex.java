package nastroje;

public final class Komplex
{
    public Komplex(double re, double im)
    {
        this.re = re;
        this.im = im;
    }
    
    public Komplex(double re)
    {
        this(re, 0);
    }
    
    public Komplex krat(Komplex b)
    {
        return new Komplex(re*b.re - im*b.im, re*b.im + im*b.re);
    }
    
    public Komplex krat(double x)
    {
        return new Komplex(x*re, x*im);
    }
    
    public Komplex minus(Komplex b)
    {
       return new Komplex(re-b.re, im-b.im); 
    }
   
    public Komplex plus(Komplex b)
    {
        return new Komplex(re+b.re, im+b.im);
    }
    
    public Komplex sqrt()
    {
        double fi = Math.atan2(im, re)/2;
        double r = Math.sqrt(Math.sqrt(re*re + im*im));
        return new Komplex(r*Math.cos(fi), r*Math.sin(fi)); 
    }
    
    public double re(){return re;}
    public double im(){return im;}
    
    public double re, im;
    
    public static final Komplex NULA = new Komplex(0,0);
    public static final Komplex I = new Komplex(0, 1);
}


