/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sehall
 */
public class Human implements Runnable{
    protected final int size = 20;
    protected double xPos, yPos;
    protected double velX, velY;
    protected double MAX_SPEED = 2.0;
    public static int PANEL_W = 600;
    public static int PANEL_H = 600;
    public int sightDistance = 100;
    protected Random generator;
    protected boolean isAlive;
    protected List<Human> humans;
    
    public Human(List<Human> list,double xPos, double yPos)
    {   generator = new Random();
        this.humans=list;
        this.xPos = xPos;
        this.yPos = yPos;
        velX = (generator.nextFloat()*2.0*MAX_SPEED-MAX_SPEED);
        velY = (generator.nextFloat()*2.0*MAX_SPEED-MAX_SPEED);
        isAlive = true;
    }
    
    
    protected void generateRandomNoiseInWorld()
    {
        velX += (generator.nextFloat()*2.0) - 1.0;
        velY += (generator.nextFloat()*2.0) - 1.0;

        if(velX > MAX_SPEED)
            velX = MAX_SPEED;
        if(velX < -MAX_SPEED)
            velX = -MAX_SPEED;
        if(velY > MAX_SPEED)
            velY = MAX_SPEED;
        if(velY < -MAX_SPEED)
            velY = -MAX_SPEED;
                 
    }
    
    protected void moveWithinWorld()
    {   xPos += velX;
        yPos += velY;   
        if((int)xPos <= size/2)
        {   velX = -velX;
            xPos = size/2;
        }
        else if((int)xPos >= PANEL_W-size/2)
        {   velX = -velX;
            xPos = PANEL_W-size/2;
        }
        if((int)yPos <= size/2)
        {  velY = -velY;   
           yPos = size/2;
        }
        else if((int)yPos >= PANEL_H-size/2) 
        {
            velY = -velY;
            yPos = PANEL_W-size/2;
        }
        
    }
    
    @Override
    public void run() {
        
        int count = 0;
        int threshold = generator.nextInt(25)+5;
        
        while(isAlive)
        {
            count++;
            if(count >=threshold)
            {   count= 0;
                threshold = generator.nextInt(25)+5;
                generateRandomNoiseInWorld();
                
                double speed = Math.sqrt((velX*velX)+(velY*velY));
                if (speed < 0.01f)
                   speed = 0.01f;
                
                double minDistance = sightDistance;
                Zombie target = null;
                Point centre = new Point((int)getX(),(int)getY());
                int numPoints=0;
                for(Human h:humans)
                {
                    if(h instanceof Zombie)
                    {   Zombie z = (Zombie)h;
                        double distance = Math.sqrt(Math.pow(z.getX()-getX(),2.0)+Math.pow(z.getY()-getY(),2.0));
                        if(distance < minDistance)
                        {   minDistance = distance;
                            target = z;
                        }
                    } 
                }
                if(target != null)
                {
                    double vx_dir = ((getX() - target.getX())/minDistance)*speed;
                    double vy_dir = ((getY() - target.getY())/minDistance)*speed;
                    velX = vx_dir;
                    velY = vy_dir;
                } 
            } 
            
            moveWithinWorld();
           
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Human.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
       
    }
    public boolean isAlive()
    {
        return isAlive;
    }
    public void kill()
    {
        isAlive = false;
    }
    public double getSize()
    {
        return size;
    }
    public double getX()
    {
        return xPos;
    }
    public double getY()
    {
        return yPos;
    }
    public void draw(Graphics g)
    { Graphics2D g2 = (Graphics2D) g;
      g2.setStroke(new BasicStroke(2));
      
      g.setColor(Color.BLUE);
      g.fillOval((int)xPos-size/2,(int)yPos-size/2,size,size);
      g.setColor(Color.BLACK);
      g.drawOval((int)xPos-size/2,(int)yPos-size/2,size,size);
    }
}
