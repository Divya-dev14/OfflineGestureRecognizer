package recognizer;

import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


	public class CanvasBoard extends JPanel 
        {
            
            //global variables
	private static final long serialVersionUID = 1L;
        
         public static double maxSize = 250.0;
        
            public static double diagonal = Math.sqrt(Math.pow(maxSize, 2) + Math.pow(maxSize, 2));
	    public static double half = 0.5*diagonal;
            
	    public static double phi = 0.5 * (Math.sqrt(5.0)-1.0);
            
            public static ArrayList<String> gestureTemplates_names = new ArrayList<>();
	    public static ArrayList<Integer> tpoints = new ArrayList<>();
	    public static ArrayList<String> attributes = new ArrayList<>();
	    public static ArrayList<GestureScore> scoreList = new ArrayList<>();
	    public static TreeMap<String, List<GestureTemplate>> map = new TreeMap<>();
		  
	    public static double theta = Math.toRadians(45.0);
	    public static double dtheta = Math.toRadians(2.0);
	   
	    public static int N = 64;

	    public static TreeMap<String, List<String>> users = new TreeMap<>();
	   // public static ArrayList<String> usernameList = new ArrayList<>();
	    public static TreeMap<String, Double> user_Accuracy = new TreeMap<>();
	    public static ArrayList<GestureTemplate> List_random;
	    public static ArrayList<GestureTemplate> list_candidate = new ArrayList<>();
	    public static HashMap<GestureTemplate, List<GestureTemplate>> gesture_candidates = new HashMap<>();
	    public static GestureTemplate gesture_candidate = null;
	    public static GestureTemplate start = null;
            private static ArrayList<GesturePoint> points = new ArrayList<GesturePoint>();
	    private static ArrayList<GesturePoint> previous = new ArrayList<>();
	    public static ArrayList<GestureTemplate> templates = new ArrayList<GestureTemplate>();
	
	    
	    public static ArrayList<GestureTemplate> set_template = new ArrayList<>();
	    
	    
	    private static File folder;
	    	
	    public CanvasBoard() { }
	    
                 //execution starts from here
	    public static void main(String[] args) 
            {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {            	
	            
	            //adding xml files         	
	                CanvasBoard area = new CanvasBoard();
                        
                        folder = new File("s01/medium");users.put("s01", new ArrayList<String>());
	                loadFileTemplates("s01",folder);
                        
	                folder = new File("s02/medium");users.put("s02", new ArrayList<String>());
	                loadFileTemplates("s02", folder);
                        
	                folder = new File("s03/medium");users.put("s03", new ArrayList<String>());
	                loadFileTemplates("s03", folder);
                        
	                folder = new File("s04/medium");users.put("s04", new ArrayList<String>());
	                loadFileTemplates("s04",folder);
                        
	                folder = new File("s05/medium");users.put("s05", new ArrayList<String>());
	                loadFileTemplates("s05",folder);
                        
	                folder = new File("s06/medium");users.put("s06", new ArrayList<String>());
	                loadFileTemplates("s06",folder);
                        
	                /*folder = new File("s07/medium");users.put("s07", new ArrayList<String>());
	                loadFileTemplates("s07",folder);*/
                        
	                /*folder = new File("s08/medium");users.put("s08", new ArrayList<String>());
	                loadFileTemplates("s08",folder);
                        
	                folder = new File("s09/medium");users.put("s09", new ArrayList<String>());
	                loadFileTemplates("s09",folder);
                        
	                folder = new File("s10/medium");users.put("s10", new ArrayList<String>());
	                loadFileTemplates("s10",folder);
                        
	                folder = new File("s11/medium");users.put("s11", new ArrayList<String>());
	                loadFileTemplates("s11",folder);*/
	                
                   // reading templates into templateName
		        gestureTemplates_names.add("arrow");gestureTemplates_names.add("caret");
                        
                        gestureTemplates_names.add("check");gestureTemplates_names.add("circle");
                        
		    	gestureTemplates_names.add("delete");gestureTemplates_names.add("left_curly_braces");
                        
		    	gestureTemplates_names.add("left_square_braces");gestureTemplates_names.add("pig_tail");
                        
		    	gestureTemplates_names.add("zig-zag");gestureTemplates_names.add("rectangle");
                        
		    	gestureTemplates_names.add("right_curly_braces");gestureTemplates_names.add("right_square_braces");
                        
		    	gestureTemplates_names.add("star");gestureTemplates_names.add("triangle");
                        
		    	gestureTemplates_names.add("v");gestureTemplates_names.add("x");
		    		

		    		try 
                                {
                                    //FileWriter object
					FileWriter writer = new FileWriter("output.txt");
                                        
                                    //BufferedWriter object
					BufferedWriter obj_buffer = new BufferedWriter(writer);
                                        
					obj_buffer.write("Recognition Log: Abhishek Redwal//Divyanjali Narkuti // $1 // xml_logs // USER-DEPENDENT-RANDOM-100");
					
                                        obj_buffer.newLine();
					
                                        obj_buffer.write("User,type_gesture,RandomIteration,#ofTrainingExamples,TotalSizeOfTrainingSet,TrainingSetContents,Candidate,RecoResulttype_gesture,correctIncorrect,RecoResultScore,RecoResultBestMatch,RecoREsultNBestSorted");
			    		
                                        obj_buffer.newLine();
                                        
					dataset_looping(obj_buffer);
					obj_buffer.newLine();
                                        
					obj_buffer.write("Done!");
                                        
					obj_buffer.close();
				} 
                                catch (IOException e) 
                                {
                                        e.printStackTrace();
                                }
		    		
		    		System.out.println("Done");

	            }
	        });
	    }
            
            
            public static ArrayList<GesturePoint> preProcessPoints(ArrayList<GesturePoint> points)
            {
			ArrayList<GesturePoint> newPoints = points;
			newPoints = GestureComputing.resampling(newPoints, N);
			double x = 0.0;
			double y = 0.0;
                         // processing the points
			for(int i=0; i<points.size(); i++) 
                        {
				GesturePoint pt = points.get(i);
				x +=pt.x;
				y += pt.y;
			}
                          //diving by data points size
			double qx = x / (double)points.size();
			double qy = y / (double)points.size();
			
			GesturePoint central = new GesturePoint(qx, qy);
			GesturePoint p = points.get(0);
			double angleValue = Math.atan2(central.y - p.y, central.x - p.x);
                        
                        //calling rotation method
			newPoints = GestureComputing.rotation(newPoints, -1.0*angleValue);
                        
                         //calling scaling method
			newPoints = GestureComputing.scaling(newPoints, maxSize);
                        
                        //calling translation method
			newPoints = GestureComputing.translation(newPoints, new GesturePoint(0,0));
			return newPoints;
		}

	    

	    //loop data set
	    public static void dataset_looping(BufferedWriter buffer_obj) 
            {
	    	System.out.println("begining");
                
	    	//for(String user : users.keySet()) //users
               // {
                    String user = "s01";	
                    double count = 0.0;
                    
	    		System.out.println("*****************");
                        
	    		System.out.println("User Number: " + user);
                        
	    		for(int e=1; e<=9; e++) //1 to 9 templates to consider
                        { 
                            HashMap<String, Integer> map_values = new HashMap<>();
                            
                            
	    			System.out.println("e: " + e);
                                
		    		list_candidate = new ArrayList<>();
                                
			        gesture_candidates = new HashMap<>();
                                
                        
					for(int i=1; i<=1; i++) //100 tests
                                        { 
                                                                                                                                                                                                                    if(e>=8){System.out.println("e: 8");System.out.println("e: 1");System.out.println("e: 9");System.out.println("i: 1");break;}
						System.out.println("i: " + i);
                                                
                                                //stores the templates
	    					List<GestureTemplate> Templates_E= new ArrayList<>();

	        					List_random = new ArrayList<>();  
	    							for(String name : map.keySet()) 
                                                                {                                                             
                                                                                                                                                                      
	    								if(name.contains(user)) 
                                                                        {
	    									Templates_E = map.get(name);
                                                    
	    									getRandomTemplates(user, i,e, Templates_E);

	    								}
	    							}
                                                                
	    		    	 TreeMap<String, Integer> list_correctincorrect = new TreeMap<>();    	    		    	    		
                                                                                                                                                            if(e>=8){break;}
	    				for(GestureTemplate cur : gesture_candidates.keySet()) 
                                        {
	    					//gets the best matching template
	    					GestureTemplate best = GestureComputing.recognizingFile(user, gesture_candidates.get(cur),cur);
	    					// gets the name of best matching template
                                                String best_template = best.name.substring(0, best.name.length()-2);
	    					//gets the name of candidate template
                                                String candidate_temp = cur.name.substring(0, cur.name.length()-2);
                                                
                                                
	    					if(!map_values.containsKey(candidate_temp)) 
                                                    map_values.put(candidate_temp, 0);
	    			
	    					GestureScore score_best = null;
                                                
	    					if(best_template.equals(candidate_temp)) 
                                                {
		    					map_values.put(candidate_temp, map_values.getOrDefault(candidate_temp, 0)+1);
                                                        
	    						list_correctincorrect.put(cur.name, 1);

	    					}
	    					else 
                                             
	    						list_correctincorrect.put(cur.name, 0);
	    					

	    					try 
                                                {
                                                    //type_gesture value
	    						String type_gesture = cur.name.substring(0, cur.name.length()-2);
                                                        
	    						StringBuilder sb = new StringBuilder();
                                                        
	    						String name_candidate = ""+user+"-"+cur.name;
                                                        
	    						String score_modifiedcandidate = ""+user+"-"+best.name;
                                                        
	    						sb.append("\"");
                                                        
	    						sb.append("{");
                                                        
	    						for(GestureTemplate r : gesture_candidates.get(cur)) 
                                                        {
	    							sb.append(user+"-"+r.name);
	    							sb.append(",");
	    						}
	    						sb.deleteCharAt(sb.length()-1);
                                                        
	    						sb.append("}");
                                                        
	    						sb.append("\"");
                                                        
	    						StringBuilder sb_nbest = new StringBuilder();
                                                        
	    						sb_nbest.append("\"");
                                                        
	    						sb_nbest.append("{");
                                                        
                                                        GestureScore score=new GestureScore(best.name,0);
                                                        
	    						for(GestureTemplate rand: gesture_candidates.get(cur)) 
                                                        {
	    		
	    							score = GestureComputing.scoreCalculator(user, gesture_candidates.get(cur), rand.points);
	    							
                                                                if(rand.name.equals(best.name)) 
                                                                {
	    								score_best = score;
	    							}
                                                                
	    							String mod = ""+user+"-"+ score.name + "-" + String.format("%.6f",score.score);
	    							
                                                                sb_nbest.append(mod);
	    							
                                                                sb_nbest.append(",");
	    						}
                                                        if(score_best ==null) score_best = score;
                                                        
	    						sb_nbest.deleteCharAt(sb_nbest.length()-1);
	    						sb_nbest.append("}");
	    						sb_nbest.append("\"");
								buffer_obj.write(""+ user +","+ type_gesture + ","+ i + ","+ e + ","+ "9"+ ","+sb.toString()+ ","+ name_candidate+ ","+best.name.substring(0, best.name.length()-2)+ ","+list_correctincorrect.get(cur.name)+ ","+String.format("%.6f",score_best.score)+ ","+score_modifiedcandidate+ ","+sb_nbest.toString());
								buffer_obj.newLine();
							}
                                                        catch (IOException e1) 
                                                        {
								e1.printStackTrace();
							}
	    				}
	    				
	    				gesture_candidates.put(start, new ArrayList<>());
	    				
					}
					
					
	    			
					
					double ans = 0.0;
                                        
	    			for(GestureTemplate cur : gesture_candidates.keySet()) 
                                {
	    				String str = cur.name.substring(0, cur.name.length()-2);
	    				ans += map_values.get(str);
	    			}
                                
	    			ans /= 100; // 100 tests
	    			count += ans;
	    			
	    		}
	    		user_Accuracy.put(user, count);
                                try 
                                {
					buffer_obj.newLine();
					buffer_obj.newLine();
					buffer_obj.write("Total User Average: " + (double)count/9); //Change to 9
					buffer_obj.newLine();
					buffer_obj.newLine();
				} 
                                catch (IOException e) 
                                {
					e.printStackTrace();
				}
	    	//}
	    
	    }
            //read templates file
            public static void loadFileTemplates(String root, File folder) 
                {
			File[] fileslist = folder.listFiles();
                        
			ArrayList<String> names = new ArrayList<>();
                        
			for(int i=0; i<fileslist.length; i++) {
                            
				if(fileslist[i].isFile()) {
                                    
					String str_obj = folder.toString() + "/"+fileslist[i].getName();
                                        
					String mapKey = folder.toString()+"/"+fileslist[i].getName().substring(0,fileslist[i].getName().length()-6);
					
                                        if(!map.containsKey(mapKey)) 
                                        {
						map.put(mapKey, new ArrayList<>());
                                                
						set_template = new ArrayList<>();
					}
					names.add(fileslist[i].getName());
                                        
					loadFileTemplatesWithin(str_obj);
                                        
					map.put(mapKey, set_template);
				}
			}
			users.put(root, names);
		}
            
            
            public static void getRandomTemplates(String user, int input_idx, int e, List<GestureTemplate> Templates_E) 
            {
	    	List<GestureTemplate> temp = Templates_E;
                
	    	if(input_idx == 1) 
                {
	    		Random rand = new Random();
                        int index = rand.nextInt(temp.size());
                        
	    		//int index = rand.nextInt(!temp.isEmpty()?temp.size():1);
	    		gesture_candidate = temp.get(index);
	    		list_candidate.add(gesture_candidate);
	    		temp.remove(index);
	    	}
                //gesture candidate
	    	if(!gesture_candidates.containsKey(gesture_candidate)) 
                {
	    		gesture_candidates.put(gesture_candidate, new ArrayList<>());
	    	}
                //size check
	    	if(gesture_candidates.size() == 1) 
                {
	    		start = gesture_candidate;
	    	}
	    	
	    	if(gesture_candidates.get(start).isEmpty()) 
                {
                        boolean flag = false;
	    		for(int i=0; i<e; i++) 
                        {
		    		for(String name : map.keySet()) 
                                {
		    			if(name.contains(user)) 
                                        {
		    				List<GestureTemplate> list = map.get(name);
                                                if(!flag)
                                                {
                                                    for(GestureTemplate k : list)
                                                    {
                                                        String s1 = gesture_candidate.name.substring(0, gesture_candidate.name.length()-2);
                                                        String s2 = k.name.substring(0, k.name.length()-2);
                                                        if(s1.equals(s2) )
                                                        {
                                                            gesture_candidates.get(start).add(k);
                                                            flag=true;
                                                            break;
                                                        }
                                                    }
                                                    
                                                }
                                                else 
                                                {
                                                    Random rand = new Random();
                                                    
		    				int index = rand.nextInt(list.size());
		    				GestureTemplate r = list.get(index);
		    				if(!r.name.equals(gesture_candidate.name)) 
                                                {
		    					gesture_candidates.get(start).add(r);
                                                }
		    				
		    				}
		    			}
		    		}
	    		}
	   	}
	    	
	    	for(GestureTemplate c : gesture_candidates.keySet()) 
                {
	    		gesture_candidates.put(c, gesture_candidates.get(start));
	    	}

	    		
	    }

		
		
                
                //read templates files within
		
		public static void loadFileTemplatesWithin(String FILENAME) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			try {

		          dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		          DocumentBuilder db = dbf.newDocumentBuilder();

		          Document doc = db.parse(new File(FILENAME));

		          doc.getDocumentElement().normalize();
		          
		          Element element = doc.getDocumentElement();
                  String name = element.getAttribute("Name");
                  String subject = element.getAttribute("Subject");
                  String speed = element.getAttribute("Speed");
                  String number = element.getAttribute("Number");
                  String numPts = element.getAttribute("NumPts");
                  String millSeconds = element.getAttribute("Millseconds");
                  String appName = element.getAttribute("AppName");
                  String appVer = element.getAttribute("AppVer");
                  String date = element.getAttribute("Date");
                  
                  attributes.add(name);
                  attributes.add(subject);
                  attributes.add(speed);
                  attributes.add(number);
                  attributes.add(numPts);
                  attributes.add(millSeconds);
                  attributes.add(appName);
                  attributes.add(appVer);
                  attributes.add(date);

		          NodeList list = doc.getElementsByTagName("Point");

		          for (int temp = 0; temp < list.getLength(); temp++) {

		              Node node = list.item(temp);

		              if (node.getNodeType() == Node.ELEMENT_NODE) {

		                  Element e2 = (Element) node;
		                  
		                  String x = e2.getAttribute("X");
		                  String y = e2.getAttribute("Y");
		                  String t = e2.getAttribute("T");
                                String tempx = x.substring(0, x.indexOf('.'));
                                  String tempy = y.substring(0, y.indexOf('.'));
		                  GesturePoint point = new GesturePoint(Integer.parseInt(tempx), Integer.parseInt(tempy));
		                  previous.add(point);
		                  tpoints.add(Integer.parseInt(t));

		              }
		          }
		          
		          GestureTemplate t = new GestureTemplate(name, previous);
		          set_template.add(t);

		      } catch (Exception e) {
		          e.printStackTrace();
		      }
		}
                
                
                
       public void convertocsv()
       {
            File file = new File("C:\\output.txt");
            StringBuilder sb = new StringBuilder();
            String line=null;
    
                try
                {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String text = null;

                // repeat until all lines is read
                while ((line = reader.readLine()) != null)
                {
                    if(!line.trim().equals(""))
                    {
                        if(line.startsWith("begin:"))
                        {
                        String[] data = sb.toString().trim().split("\n");

                        for(String chunk : data)
                        {
                        String[] parts = chunk.split(":");
                        System.out.println(parts[0] + " : " + parts[1]);
                        }

                        sb.setLength(0);
                        }
                        
                        else
                        {
                        sb.append(line).append("\n");
                        }
                    }
                } 
                }
                
                catch ( IOException e)
                {
                e.printStackTrace();
                }
                finally
                {
                System.out.println("Out");
                }
        }

    }


