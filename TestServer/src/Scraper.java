

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.safety.*;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Document.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper {
	public ArrayList<String> getData(String building,String room){
		String url = "https://lslab.ics.purdue.edu/icsWeb/LabInfo?building=";
		url = url + building;
		url = url + "&room=" + room;
		ArrayList<String> data = new ArrayList<String>();
		try{
			Document document = Jsoup.connect(url).get();
			Elements stuff = document.select("span.site_md");
			for (Element a : stuff) {
				System.out.println("\n" + a.text().replace("\u00a0", "") );
				data.add(a.text().replace("\u00a0", ""));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		return data;
		
	}
	public static void main(String[] args){
		String url = "https://lslab.ics.purdue.edu/icsWeb/LabInfo?building=BCC&room=204";
		System.out.println("Fetching " + url);
		ArrayList<String> String = new ArrayList<String>();
		try {
			Document document = Jsoup.connect(url).get();
			System.out.println("Trying....");
			Elements stuff = document.select("span.site_md");
			for (Element a : stuff) {
				System.out.println("\n" + a.text().replace("\u00a0", "") );
				String.add(a.text().replace("\u00a0", ""));
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		
	}

}
