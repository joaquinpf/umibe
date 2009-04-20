package ar.com.umibe.core.filter;

/**
 * @author Joaquín Alejandro Pérez Fuentes
 */
public interface IFilterWindow {
	public Filter getFilter();
	public void setFilter(Filter f);	
	public void setVisible(boolean b);
}
