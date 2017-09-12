//IMPORTS
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

//CLASS
/**
 * This class read a single planet coaster xml file storing the data in two arrays: Keys and Values
 * The Keys array is made of Strings because the translations Keys are simple strings
 * The translated phrase is stored into a byte[] because i can store it in UTF-8
 * */
class PlanetCoasterMerge {
    ArrayList<String> Keys; //The translation Keys
    ArrayList<byte[]> utf8_values; //The translation values
    private boolean isnewFile; //if is the new file i need to store comments

    /**
     * The constructor method open the file and call a method to fill the arrays
     * @param file_top_open the path with the filename to open
     * @param isfinalfile if is the final file i need to consider comments
     * @throws Exception throw an invalid UTF-8 charset exception (from scan_current_node()) or a generic Exception if the XML file is not valid
     */
    PlanetCoasterMerge(String file_top_open, boolean isfinalfile) throws Exception{
        Element root; //The root element of the xml file
        Document document_file;
        isnewFile=isfinalfile; //if is the new file i need to store comments
        //creating the array lists
        Keys=new ArrayList<String>();
        utf8_values = new ArrayList<byte[]>();
        //-------------------------------------------------------------------------
        try{
            //Opening the file
            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
            DocumentBuilder domParser = dbf.newDocumentBuilder();
            document_file=domParser.parse(new File(file_top_open));
            root=document_file.getDocumentElement();
            Node start_node=root.getFirstChild();
            //Choosing the First node
            if(!start_node.getNodeName().equals("translation")){
                start_node=start_node.getNextSibling();
                if(!start_node.getNodeName().equals("translation")){
                    throw new Exception("Bad PlanetCoaster-XML File! Check your XML file"); //can't find the start node, error
                }
            }
            scan_current_node(start_node); //starting from the root
        }catch(SAXParseException e){ //error reading the xml file
            throw new Exception("Error-->"+e.getMessage());
        }catch(FileNotFoundException e){ //error opening the file
            throw new Exception("Error file not found-->"+e.getMessage());
        }catch(Exception e){ //generic error
            e.printStackTrace();
            throw new Exception("Generic Error-->"+e.getMessage());
        }
    }//Constructor

    /**
     * This method scan all the nodes under the root
     * Scanning all the nodes this method fill the two arrays with all the values
     * @param node the root node, where i start to scan
     * @throws UnsupportedEncodingException if the string format is not valid
     */
    private void scan_current_node(Node node) throws UnsupportedEncodingException{
        System.out.println("---------------------------");
        System.out.println("We're in the XML File");
        NodeList child_nodes_list=node.getChildNodes(); //Entering root sons
        int childs_count = child_nodes_list.getLength();
        System.out.println("Childs:"+childs_count); //Counting childrens
        for(int i=0;i<childs_count;i++){ //For every child
            node=child_nodes_list.item(i); //I get the node
            if(node.getNodeName().equals("entry")) { //if is an entry
                NamedNodeMap node_att = node.getAttributes(); //Checking tag attrs
                if (node_att.getLength() > 0) { //The entry has attrs, save them
                    Keys.add(((Attr) node_att.item(0)).getValue());
                    utf8_values.add(((Attr) node_att.item(1)).getValue().getBytes(Charset.forName("UTF-8")));
                }
            }else{ //if is a comment
                if(isnewFile) { //store comments only if is the new file
                    if (node.getNodeType() == Element.COMMENT_NODE) {
                        Comment comment = (Comment) node;
                        Keys.add("Comment");
                        utf8_values.add(comment.getData().getBytes(Charset.forName("UTF-8")));
                    }
                }//is_new_file
            }//else_is_a_comment
        }//for_end
        System.out.println("Arrays Created!");
        System.out.println("---------------------------");
    }
    //----------------------------------------------------------
}//end_class
