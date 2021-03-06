/*
 * Copyright 2006-2013 Lin.k N.V. All rights reserved.
 */

package com.wgzhao.dbfreader.ui.graphical;

import com.wgzhao.dbfreader.domain.Config;
import com.wgzhao.dbfreader.domain.Constants;
import com.wgzhao.dbfreader.domain.Controller;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.io.IOException;

/**
 * User: gvhoecke <gianni.vanhoecke@lin-k.net>
 * Date: 16/10/13
 * Time: 12:05
 */
public class AboutPanelContainer
        extends JDialog
{

    public AboutPanelContainer(Frame frame, boolean b)
    {

        super(frame, b);

        this.setTitle("About");
        this.setModal(true);
        this.getRootPane().putClientProperty("Window.style", "small");

        this.setMinimumSize(Constants.ABOUT_DIMENSTION);
        this.setSize(Constants.ABOUT_DIMENSTION);
        this.setPreferredSize(Constants.ABOUT_DIMENSTION);

        try {

            this.setIconImage(new ImageIcon(
                    ImageIO.read(getClass().getResource("/database.png"))).getImage());
        }
        catch (IOException e) {

            //Ignore
        }

        this.initLayout();

        this.pack();
        this.setLocationRelativeTo(frame);
        this.setVisible(true);
    }

    private void initLayout()
    {

        this.setLayout(new BorderLayout());

        //Image
        JLabel imageLabel = new JLabel("");
        try {

            imageLabel.setIcon(new ImageIcon(
                    ImageIO.read(getClass().getResource("/database_128.png"))));
        }
        catch (IOException e) {

            imageLabel.setText("DBF Reader");
        }
        add(imageLabel, BorderLayout.WEST);

        //Info
        JLabel infoLabel = new JLabel(
                "<html>" +
                        "<h3>" + Controller.TITLE + " v" + Config.getApplicationVersion() + "</h3>" +
                        "<p>By Gianni Van Hoecke &lt;gianni.vh@gmail.com&gt;</p><br />" +
                        "<p>This app uses:</p>" +
                        "<ul style=\"font-size: small; margin: 15px;\">" +
                        "<li>Jamel DBF Library<br /><span style=\"font-size: x-small;\">(https://github.com/jamel/dbf)</span></li>" +
                        "<li>Mono Icons<br /><span style=\"font-size: x-small;\">(http://www.tutorial9.net/downloads/108-mono-icons-huge-set-of-minimal-icons/)</span></li>" +
                        "<li>Receptionist icon<br /><span style=\"font-size: x-small;\">(https://www.iconfinder.com/icons/40094/receptionist_icon)</span></li>" +
                        "</ul>" +
                        "</html>");
        add(infoLabel, BorderLayout.CENTER);
    }
}
