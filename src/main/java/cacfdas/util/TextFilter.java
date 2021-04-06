/*
 * TextFilter.java
 *
 * Created on April 6, 2006, 4:23 PM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */
public class TextFilter extends javax.swing.filechooser.FileFilter
{
       public boolean accept(java.io.File f)
       {
           String ext = getExtension(f);
                    
            if ( f.isDirectory() )
               return true;
            else if ( ext == null)
            {
                return false;
            }
            else if ( ext.equals("txt") || ext.equals("text"))
                return true;
            else
                return false;
        }
                
        public String getDescription()
        {
            return new String("*.txt, *.text");
        }
                
        private String getExtension(java.io.File f) 
        {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');
            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }          
    }
