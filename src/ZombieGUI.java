

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 *
 * @author sehall
 */
public class ZombieGUI extends JPanel implements ActionListener{
    
    private Timer timer;
    private JButton addHuman, addZombie;
    private DrawPanel drawPanel;
    private List<Human> humans;
    
    
    public ZombieGUI()
    {   super(new BorderLayout());
        try
        {  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e){}

        
        timer = new Timer(20,this);
        humans = Collections.synchronizedList(new ArrayList<>());
        addHuman = new JButton("Add Human");
        addZombie= new JButton("Add Zombie");
        addHuman.addActionListener((ActionListener)this);
        addZombie.addActionListener((ActionListener)this);
        drawPanel = new DrawPanel();
        
        super.add(drawPanel,BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel();
        southPanel.add(addZombie);
        southPanel.add(addHuman);
        super.add(southPanel,BorderLayout.SOUTH);
       
        
        timer.start();
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if(source == addHuman)
        {
            Human human = new Human(humans,drawPanel.getWidth()/2, drawPanel.getHeight()/2);
            Thread t = new Thread(human);
            t.start();
            humans.add(human);
        
        }
        if(source == addZombie)
        {
            Zombie zombie = new Zombie(humans,drawPanel.getWidth()/2, drawPanel.getHeight()/2);
            Thread t = new Thread(zombie);
            t.start();
            humans.add(zombie);
        
        }
        
        for(int i=0;i<humans.size();i++)
        {
            Human human = humans.get(i);
            if(!human.isAlive())
            {   human = new Zombie(humans,human.getX(), human.getY());
                humans.set(i, human);
                Thread t = new Thread(human);
                t.start();
            }
        }
        drawPanel.repaint();
    }
    
    private class DrawPanel extends JPanel
    {
        public DrawPanel()
        {   super();
            super.setBackground(Color.white);
            super.setPreferredSize(new Dimension(Human.PANEL_W,Human.PANEL_H));
        }
        
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Human.PANEL_H = getHeight();
            Human.PANEL_W = getWidth();
            for(Human human:humans)
                human.draw(g);

        }
    }
    
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Fish Bowl");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ZombieGUI());
        frame.pack();                                      //pack frame
        frame.setVisible(true);                                      //show the frame
    }

}
