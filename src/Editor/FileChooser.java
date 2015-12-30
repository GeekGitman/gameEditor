/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Editor;
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;  
import java.io.*;  
  
import javax.swing.JButton;  
import javax.swing.JFileChooser;  
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.util.*;

/**
 *
 * @author yc
 */
public class FileChooser extends javax.swing.filechooser.FileFilter{  
    JButton open=null;  
    String path;
    File selected;
    String name;
    public static void main(String[] args) {  
        new FileChooser();  
    }  
    public FileChooser(){  
        path="";  
    }  
    public String getPath(){
       return path;
    }
    public File getFile(){
        return selected;
    }
    public String getName(){
        return name;
    }
    public void newmap() {  
        // TODO Auto-generated method stub  
        //读取背景
        JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY );  
        //jfc.setFileFilter(this);
        jfc.setCurrentDirectory(new File("./src/background"));
        javax.swing.filechooser.FileFilter filefilter = new javax.swing.filechooser.FileFilter(){
            public boolean accept(File file){
                if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")){
                    return true;
                }
                else return false;
            }
            public String getDescription(){
                return ".jpg/.png";
            }
        };
        jfc.setFileFilter(filefilter);
        jfc.showOpenDialog(null);  
        File file=jfc.getSelectedFile(); 
        if(file!=null && file.isFile()){
            path="./src/background/"+file.getName(); 
            selected=file;
        }
    }  
    
    public void openfile(){
        JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );  
        jfc.setCurrentDirectory(new File("./src/maps"));
        jfc.showOpenDialog(new JButton());  
        File file=jfc.getSelectedFile(); 
        if(file!=null && file.isDirectory()){
            path=file.getAbsolutePath();   
            selected=file;
        }
    }
    
    public void open(ArrayList maplist){
        maplist.clear();        //先将map清空
        String kind="";
        try {
            File dir = new File(path);
            String[] filename=dir.list();
            for(int i=0;i<filename.length;i++){
            map thismap = new map();
            thismap.setName(dir.getName().substring(10));
            InputStreamReader read = new InputStreamReader( new FileInputStream(path+"\\"+filename[i]), "UTF-8");
            BufferedReader reader = new BufferedReader(read);
            String line;
            line = reader.readLine();
            String[] ss = line.split(" ");
            int me_x = Integer.parseInt(ss[0]) - 1;
            int me_y = Integer.parseInt(ss[1]) - 1;
            int exit_x = Integer.parseInt(ss[2]) - 1;
            int exit_y = Integer.parseInt(ss[3]) - 1;
            thismap.addone(me_x, me_y, "start");
            thismap.addone(exit_x, exit_y, "end");
            thismap.addmap(reader.readLine(), "background");
            while(((line = reader.readLine()) != null) ){
                ss=line.split(" ");
                if(ss[0].matches("b1")) kind="block";
                else if(ss[0].matches("b2")) kind="earth";
                else if(ss[0].matches("t1")) kind="stab";
                else if(ss[0].matches("m1")) kind="moster";
                else;
                thismap.addone(Integer.parseInt(ss[1]), Integer.parseInt(ss[2]), kind);
            }
            maplist.add(thismap);
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void savefile(){
        /*
        JFileChooser jfc=new JFileChooser();  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.setFileFilter(this);
        jfc.showSaveDialog(null);  
        File file=jfc.getSelectedFile();
        if(file!=null){
            String name = file.getName();
            String folder = file.getAbsolutePath();
            folder = folder.replaceAll(name, "");
            name = name+".map";
            path=folder+name;
        }*/
        
    }
    
    public void save(map thismap){
        //新建输入名字
        String guanqia = JOptionPane.showInputDialog(null, null, "请输入关卡数：（正整数）",JOptionPane.INFORMATION_MESSAGE );
        if(!guanqia.isEmpty()){
            try{
                Integer.parseInt(guanqia);
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "输入的必须是数字.", "alert", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        thismap.setName(guanqia);
        path="./src/maps/scene"+thismap.getName()+".txt";
        int me_x=0,me_y=0,exit_x=1,exit_y=1;
        String backgroundpath="";
        ArrayList contents = thismap.getmap();
        Iterator it1=contents.iterator();
        if(contents.size()<3){
            JOptionPane.showMessageDialog(null, "There are no start point or end point or background. Cannot save.", "alert", JOptionPane.ERROR_MESSAGE);
            return;
        }
        while(it1.hasNext()){
            map.blocks block_buff=(map.blocks)it1.next();
            if(block_buff.getkind().matches("start")){
                me_x=block_buff.getX();
                me_y=block_buff.getY();
            }
            else if(block_buff.getkind().matches("end")){
                exit_x=block_buff.getX();
                exit_y=block_buff.getY();
            }
            else if(block_buff.getkind().matches("background")){
                backgroundpath=block_buff.getURL();
            }
            else {};
        }
        it1=contents.iterator();
        try {  
            FileOutputStream out=new FileOutputStream(path);
            PrintStream p=new PrintStream(out);
            p.println((me_x+1)+" "+(me_y+1)+" "+(exit_x+1)+" "+(exit_y+1));
            p.println(backgroundpath);
            String s="";
            while(it1.hasNext()){
                map.blocks block_buff=(map.blocks)it1.next();
                if(block_buff.getkind().matches("earth")) s="b2";
                else if(block_buff.getkind().matches("block")) s="b1";
                else if(block_buff.getkind().matches("stab")) s="t1";
                else if(block_buff.getkind().matches("monster")) s="m1";
                else if(block_buff.getkind().matches("trigger")) s="t2";
                else continue;
                if(block_buff.getkind().matches("trigger"))
                    p.println(s+" "+(block_buff.getX()+1)+" "+(block_buff.getY()+1)+" "+(block_buff.getx2()+1)+" "+(block_buff.gety2()+1));
                else
                    p.println(s+" "+(block_buff.getX()+1)+" "+(block_buff.getY()+1));
            }
        }  
        catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    @Override
    public boolean accept(java.io.File f) {
        if (f.isDirectory()) return true;
        return f.getName().endsWith(".map");
    } 
    @Override
    public String getDescription(){
        return ".map";
    }

}  

