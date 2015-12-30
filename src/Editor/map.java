/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Editor;
import java.util.*;
/**
 *
 * @author yc
 */
public class map {
    private ArrayList maps;
    static int numofmap=0;
    private int index;
    public String kind_buff;
    private String name;
    
    public class blocks{
        private String kind="";
        private int x=0;
        private int y=0;
        private int x2=0,y2=0;
        private String url="";
        public blocks(int x,int y,String kind){
            this.x=x;
            this.y=y;
            this.kind=kind;
            url="";
        }
        public blocks(String URL,String kind){
            url=URL;
            this.kind=kind;
            x=-1;y=-1;
        }
        public blocks(int x1,int x2,int y1,int y2){
            this.x=x1;this.y=y1;
            this.x2=x2;this.y2=y2;
            this.kind="trigger";
        }
        public String getkind(){
            return kind;
        }
        public int getX(){
            return x;
        }
        public int getY(){
            return y;
        }
        public int getx2(){
            return x2;
        }
        public int gety2(){
            return y2;
        }
        public String getURL(){
            return url;
        }
    }
    
    
    public map(){
        numofmap++;
        maps=new ArrayList();
        index=numofmap;
        name="";
    }
    public void addone(int x,int y,String kind){
        if(kind=="start" || kind=="end"){
            maps.add(0, new blocks(x,y,kind));
            return;
        }
        else
            maps.add(new blocks(x,y,kind));
    }
    public void addmap(String URL,String kind){
        maps.add(0,new blocks(URL,kind));
    }
    public void deleteone(int x,int y){
        Iterator it = maps.iterator();
        kind_buff="";
        while(it.hasNext()){
            blocks buffer=(blocks)it.next();
            if(buffer.x==x && buffer.y==y){
                it.remove();
                kind_buff=buffer.getkind();
                System.out.println(kind_buff);
            }
        }
    }
    public void deletemap(){
        Iterator it = maps.iterator();
        while(it.hasNext()){
            blocks buffer=(blocks)it.next();
            if(buffer.getkind().matches("background")){
                it.remove();
                return;
            }
        }
    }
    public void addTriggers(int x1,int x2,int y1,int y2){
        maps.add(new blocks(x1,x2,y1,y2));
    }
    public void delTriigers(int x1,int x2,int y1,int y2){
        Iterator it = maps.iterator();
        while(it.hasNext()){
            blocks buffer=(blocks)it.next();
            if(buffer.x==x1 && buffer.y==y1 && buffer.x2==x2 && buffer.y2==y2){
                it.remove();
            }
        }
    }
    public ArrayList getmap(){
        return maps;
    }
    public void clear(){
        maps.clear();
    }
    public int getIndex(){
        return index;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }
}
