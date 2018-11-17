/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xfighter.GUI;


public class ListNpc {
    
    public String name;
    public int id;

    public String getName() {
        return name;
    }
    
    public ListNpc(String name, int id) {
        this.name = name;
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
    
    @Override
    public String toString() {
        return name + " " + id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean sameSame = false;

        if (obj != null && obj instanceof ListNpc)
        {
            sameSame = ((this.name == ((ListNpc) obj).name) 
                    && (this.id == ((ListNpc) obj).id));
        }

        return sameSame;
    }
    
    
    
}
