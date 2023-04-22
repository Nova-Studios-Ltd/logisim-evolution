/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.gui.menu;

import com.cburch.logisim.net.Server;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import static com.cburch.logisim.gui.Strings.S;

class MenuNetwork extends JMenu implements ActionListener {

  private static final long serialVersionUID = 1L;
  private final LogisimMenuBar menubar;
  private final JMenuItem startServer = new JMenuItem();
  private final JMenuItem stopServer = new JMenuItem();

  private Server server;

  public MenuNetwork(LogisimMenuBar menubar) {
    this.menubar = menubar;

    startServer.addActionListener(this);
    stopServer.addActionListener(this);

    this.add(startServer);
    this.add(stopServer);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final var src = e.getSource();
    if (src == startServer) {
      System.out.println("Pseudostarted Server");
      if (server == null) {
        server = new Server("", -1);
        if (server.Init()) {
          server.listen();
        }
      }
    }
    else if (src == stopServer) {
        System.out.println("Pseudostopped Server");
        if (server != null) {
          try {
            server.close();
          } catch (IOException ex) {
            System.out.println("Couldn't stop the internal socket, ignoring...");
          }
          server = null;
        }
    }
  }

  public void localeChanged() {
    this.setText(S.get("networkMenu"));
    startServer.setText(S.get("networkStartServerItem"));
    stopServer.setText(S.get("networkStopServerItem"));
  }
}
