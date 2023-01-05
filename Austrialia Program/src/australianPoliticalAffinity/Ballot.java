/**
 * This class represents the important information needed from each ballot
 * This method is used by the party class to store the information of all ballots that ranked a party first
 * @author Marc D'Avanzo
 */
package australianPoliticalAffinity;

public class Ballot {
	private int rankOneIndex;
	private int rankTwoIndex;
	private String rankTwoPartyName;
	
	/**
	 * @param none
	 * @return void
	 * This constructor will add default values to the ballot objects instance variables
	 */
	public Ballot()
	{
		rankOneIndex =-1;
		rankTwoIndex = -1;
		rankTwoPartyName = "";
	}
	/**
	 * @param one
	 * @param two
	 * @param name
	 * @return void
	 * This constructor will fill out the ballot instance variables with the parameter values
	 */
	public Ballot(int one, int two, String name)
	{
		rankOneIndex = one;
		rankTwoIndex = two;
		this.rankTwoPartyName = name;
	}
	
	/**
	 * @param index
	 * @return void
	 * This method will set rankOneIndex to the parameter value
	 */
	public void setRankOne(int index)
	{
		rankOneIndex = index;
	}
	/**
	 * @param index
	 * @return void
	 * This method will set rankTwoIndex to the parameter value
	 */
	public void setRankTwo(int index)
	{
		rankTwoIndex = index;
	}
	/**
	 * @param result
	 * @return void
	 * This method will set rankTwoPartyName to the parameter String
	 */
	public void setPartyName(String result)
	{
		rankTwoPartyName = result;
	}
	/**
	 * @param none
	 * @return rankTwoIndex
	 * This method will return the int rankTwoIndex
	 */
	public int getRankTwo()
	{
		return rankTwoIndex;
	}
	/**
	 * @param none
	 * @return rankOneIndex
	 * This method will return the int rankOneIndex
	 */
	public int getRankOne()
	{
		return rankOneIndex;
	}
	/**
	 * @param none
	 * @return rankTwoPartyName
	 * This method will return the String rankTwoPartyName
	 * 
	 */
	public String getRankTwoPartyName()
	{
		return rankTwoPartyName;
	}
}
