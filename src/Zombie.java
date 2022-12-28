
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Seth
 */
public class Zombie extends Human{

    
    public Zombie(List<Human> list, double xPos, double yPos)
    {   super(list,xPos,yPos);
        generator = new Random();
        this.humans = list;
        MAX_SPEED /=2;
        velX = (generator.nextFloat()*2.0*MAX_SPEED-MAX_SPEED);
        velY = (generator.nextFloat()*2.0*MAX_SPEED-MAX_SPEED);
    }
    
    
    @Override
    public void run() {
        
        int count = 0;
        int threshold = generator.nextInt(25)+5;
        Human target = null;
        while(isAlive)
        {
            count++;
            if(count >=threshold)
            {    count= 0;
                 threshold = generator.nextInt(25)+5;
                 generateRandomNoiseInWorld();
                 
                double speed = Math.sqrt((velX*velX)+(velY*velY));
                if (speed < 0.01f)
                   speed = 0.01f;
                
                double minDistance = PANEL_W;
                target = null;
                for(Human h:humans)
                {
                    if(h instanceof Zombie == false)
                    {   double distance = Math.sqrt(Math.pow(h.getX()-getX(),2.0)+Math.pow(h.getY()-getY(),2.0));
                        if(distance < size)
                             h.kill(); 
                        if(distance < minDistance)
                        {   minDistance = distance;
                            target = h;   
                        }
                    }
                }
                if(target != null)
                {
                    double vx_dir = ((target.getX() - getX())/minDistance)*speed;
                    double vy_dir = ((target.getY() - getY())/minDistance)*speed;
                    velX = vx_dir;
                    velY = vy_dir;
                }
                // calculate direction of boid normalised to length BOID_LENGTH/2
                
            } 
            
            moveWithinWorld();
            //adjust away from zombbie
            
            
            try {
                Thread.sleep(15);
            } catch (InterruptedException ex) {
                Logger.getLogger(Human.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
 
       
    }
    @Override
    public void draw(Graphics g)
    {   Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
      
        g.setColor(Color.RED);
        g.fillRect((int)xPos-size/2,(int)yPos-size/2,size,size);
        g.setColor(Color.DARK_GRAY);
        g.drawRect((int)xPos-size/2,(int)yPos-size/2,size,size);
    }
}
