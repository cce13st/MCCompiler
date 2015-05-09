package Absyn;
import Symbol.Symbol;
public class CallExp extends Exp {
   public Symbol func;
   public ArgList args;
   public CallExp(int p, Symbol f) {pos=p; func=f; args = null; }
   public CallExp(int p, Symbol f, ArgList a) {pos=p; func=f; args=a;}
}
