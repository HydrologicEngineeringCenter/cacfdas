/*
 * UniversalFilter.java
 *
 * Created on April 6, 2006, 4:20 PM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */

public class UniversalFilter extends javax.swing.filechooser.FileFilter
{ 
       public boolean accept(java.io.File f)
       {
           return true;
       }
                
       public String getDescription()
       {
           return new String("*.*");
       }                          
}
