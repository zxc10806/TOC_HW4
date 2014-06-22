/*
Name : 邱仲毅
Student ID: F74991722


*/

import org.json.*;
import java.net.*;
import java.io.*;
import java.util.*;
public class TocHw4{
		static HashMap<String,Integer> time;
		static HashMap<String,Info> info;
		static ArrayList<String> maxM;
		public static class Info
		{
				int maxprice;
				int minprice;
				HashMap<Integer,Integer>itime;
				ArrayList<Integer> A;
				int months = 0;
				public Info()
				{
						months = 0;
						maxprice = 0;
						minprice = -1;
						A = new ArrayList<Integer>();
						itime = new HashMap<Integer,Integer>();

				}
				public Info(int price,int month)
				{
						months = 1;
						maxprice = price;
						minprice = price;
						A = new ArrayList<Integer>();
						itime = new HashMap<Integer,Integer>();
						itime.put(month,1);
						A.add(month);
				}
				public void add(int price,int month)
				{
						if(price>maxprice)
						{
								maxprice = price;
						}
						if(price<minprice)
						{
								minprice = price;
						}
						if(itime.get(month)==null)
						{
								itime.put(month,1);
								months++;

						}
				}
		}
		public static  String GetRoadName(String Input)
		{
				int i;
				for(i=1;i<Input.length();i++)
						if((Input.charAt(i)=='路'||Input.charAt(i)=='街'||Input.charAt(i)=='巷'||(Input.charAt(i-1)=='大'&&Input.charAt(i)=='道'))&&i>3)
						{
								
								return Input.substring(0,i+1);
						}
				return null;
		}
		public static  void main(String [] args)
		{

				try{
						String sURL = new String(args[0]);

						// Connect to the URL using java's native library

						URL url = new URL(sURL);
						HttpURLConnection request = (HttpURLConnection) url.openConnection();
						request.setRequestProperty("Accept-Charset", "UTF-8");
						request.connect();
						JSONTokener jt = new JSONTokener(new InputStreamReader((InputStream)request.getContent()));
						JSONArray rootobj = new JSONArray(jt);
						int max_distinct_month = 0;
						time = new HashMap<String,Integer>();
						info = new HashMap<String,Info>();
						maxM = new ArrayList<String>();

						HashMap<String,Integer>in = new HashMap<String,Integer>();
						for(int i = 0;i< rootobj.length();i++)
						{
								JSONObject j = rootobj.getJSONObject(i);
								String jloc = j.getString("土地區段位置或建物區門牌");
								{
										String jRoad = GetRoadName(jloc);
										if(jRoad==null)
											continue;
										int price = j.getInt("總價元");
										int month = j.getInt("交易年月");
										Info tmp;
										in.put(jRoad,0);
										if(time.get(jRoad)!=null)
										{
												tmp = info.get(jRoad);
												tmp.add(price,month);	
										}
										else
										{
												time.put(jRoad,i+1);
												info.put(jRoad,new Info(price,month));
												tmp = info.get(jRoad);
										}

								}
						}
						for(int i = 0;i< rootobj.length();i++)
						{
								JSONObject j = rootobj.getJSONObject(i);
								String jloc = j.getString("土地區段位置或建物區門牌");
								String jRoad = GetRoadName(jloc);
								if(jRoad==null)
										continue;
								Info tmp = info.get(jRoad);
								if(tmp.months>max_distinct_month)
								{
										maxM.clear();
										maxM.add(jRoad);
										in.put(jRoad,1);
										max_distinct_month = tmp.months;
								}
								else if(tmp.months==max_distinct_month&&in.get(jRoad)!=1)
								{
										maxM.add(jRoad);
										in.put(jRoad,1);
								}

						}
						for(int i = 0;i<maxM.size();i++)
						{
								Info tmp = info.get(maxM.get(i));
								System.out.println(maxM.get(i)+" 最高成交價:"+tmp.maxprice+" 最低成交價:"+tmp.minprice);
						}


				}

				catch(Exception e)

				{

						System.out.println(e.getMessage());

				}

		}

}
