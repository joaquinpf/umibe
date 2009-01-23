package ar.com.umibe.core.filter;
import java.awt.Frame;


/**
 * @author Joaqu�n Alejandro P�rez Fuentes
 */
public class tester {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			Object[] params = { new Frame(), true };
			Class[] paramsClass = {Frame.class, boolean.class};
			IFilterWindow i = (IFilterWindow) Class.forName("DefaultFilterWindow").getConstructor(paramsClass).newInstance(params);

            i.setVisible(true);
		} catch (Exception e) {

		}
	}
	
	@SuppressWarnings("unchecked")
	public Filter configFilter(Filter filter) {
		try {
			Object[] params = { this, true };
			Class[] paramsClass = {Frame.class, boolean.class};
			IFilterWindow i = (IFilterWindow) Class.forName(filter.getWindow()).getConstructor(paramsClass).newInstance(params);
			i.setVisible(true);
			return i.getFilter();
		} catch (Exception e) {
			return filter;
		}
	}

}
