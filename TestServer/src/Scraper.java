

import org.jsoup.Jsoup;
import org.jsoup.safety.*;
import org.jsoup.nodes.*;
import org.jsoup.nodes.Document.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper {
	public static void main(String[] args){
		String url = "https://lslab.ics.purdue.edu/icsWeb/LabInfo?building=GRIS&room=121";
		System.out.println("Fetching " + url);
		try {
			Document document = Jsoup.connect(url).get();
			System.out.println("Trying....");
			Elements stuff = document.select("span.site_md");
			for (Element a : stuff) {
				System.out.println("\n" + a.text().replace("\u00a0", "") );
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}
		
		
	}

}
