// Borrowed from http://groovy.329449.n5.nabble.com/Elegant-quot-Groovy-quot-Way-to-Pretty-Print-List-Map-Structures-td355608.html
// thanks Jim White

import java.io.PrintWriter;
import java.io.Writer;
import org.codehaus.groovy.tools.Utilities;

public class IndentWriter extends PrintWriter
{
   protected boolean needIndent = true;
   protected String indentString;
   protected int indentLevel = 0;
   
   public IndentWriter(Writer w) { this(w, "   ", 0, true); }
   public IndentWriter(Writer w, String indent, int level, boolean needs)
   { super(w); indentString = indent; indentLevel = level; needIndent = needs }
   
   public int getIndent() { return indentLevel; }
   
   public IndentWriter plus(int i) {
      return new IndentWriter(out, indentString, indentLevel + i, needIndent);
   }
   
   public IndentWriter minus(int i) {
      return (plus(-i));
   }

   public IndentWriter next() { return plus(1); }
   public IndentWriter previous() { return minus(1); }
   
   protected void printIndent() {
      needIndent = false;
      super.print(Utilities.repeatString(indentString, indentLevel));
   }
   
   protected void checkIndent() { if (needIndent) { needIndent = false; printIndent(); }; }
   
   public void println() { super.println(); needIndent = true; }

   public void print(boolean b) { checkIndent();  super.print(b); }
   public void print(char c)    { checkIndent();  super.print(c); }
   public void print(char[] s)  { checkIndent();  super.print(s); }
   public void print(double d)  { checkIndent();  super.print(d); }
   public void print(float f)   { checkIndent();  super.print(f); }
   public void print(int i)     { checkIndent();  super.print(i); }
   public void print(long l)    { checkIndent();  super.print(l); }
   public void print(Object obj) { checkIndent();  super.print(obj); }
   public void print(String s)  { checkIndent();  super.print(s); }
}

class PrettyWriter extends IndentWriter
{
   public PrettyWriter(Writer w) { super(w, '    ', 0, true); }
   public PrettyWriter(Writer w, String ins, int level, boolean needsIt) { super(w, ins, level, needsIt); }
   public PrettyWriter plus(int i) { return new PrettyWriter(out, indentString, indentLevel + i, needIndent) }

   public void print(Collection list) {
      println('[')
      (this + 1).withWriter { indent ->
         list.each { indent.print it ; indent.println ',' }
      }
      print(']')
   }
   public void println( Collection list) { print list; println() }

   public void print(Map map) {
      println('[')
      (this + 1).withWriter { indent ->
         map.entrySet().each {
             indent.print it.key; indent.print ' : '; 
             if(it.value instanceof CharSequence) {
                 (indent + 1).print('"'+it.value.toString().replaceAll('"','\\\\"')+'"')
             } else {
                 (indent + 1).print it.value
             }
             
             indent.println ',' //';'
         }
      }
      print(']')
   }
   public void println(Map map) { print map; println() }
} 