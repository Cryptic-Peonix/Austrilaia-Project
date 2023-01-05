/**
 * This class is used to determine which of two strings comes first alphabetically
 * 
 */
package australianPoliticalAffinity;


import java.util.Comparator;

public class AlphabeticalOrderComparator implements Comparator {
	/**
	 * @param o1
	 * @param o2
	 * @return int
	 *@Override
	 *This method will compare two objects(made for Strings) and return an in value saying which object comes first alphabetically
	*/
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		
		if(s1.compareTo(s2) > 0)
		{
			return 1;
		}
		else if( s1.compareTo(s2) < 0)
		{
			return -1;
		}
		else return 0;
	}

	

}
