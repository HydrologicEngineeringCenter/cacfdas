/*
 * FileFilters.java
 *
 * Created on May 26, 2006, 10:28 AM
 */

package cacfdas.util;

/**
 *
 * @author b4edhdwj
 */
public class FileFilters {
    
    /** Creates a new instance of FileFilters */
    public FileFilters() {
    }
    
    static public UniversalFilter universal = new UniversalFilter();
    static public TextFilter text = new TextFilter();
    static public DSSFileFilter dss = new DSSFileFilter();
}
