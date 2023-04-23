/*
 * Logisim-evolution - digital logic design tool and simulator
 * Copyright by the Logisim-evolution developers
 *
 * https://github.com/logisim-evolution/
 *
 * This is free software released under GNU GPLv3 license
 */

package com.cburch.logisim.gui.menu;

import com.cburch.logisim.net.ShareClient;
import com.cburch.logisim.net.ShareServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.cburch.logisim.gui.Strings.S;

class MenuNetwork extends JMenu implements ActionListener {

  private static final long serialVersionUID = 1L;
  private final LogisimMenuBar menubar;
  private final JMenuItem startServer = new JMenuItem();
  private final JMenuItem stopServer = new JMenuItem();

  private final JSeparator separator1 = new JSeparator();

  private final JMenuItem testConnect = new JMenuItem();


  private ShareServer server;

  public MenuNetwork(LogisimMenuBar menubar) {
    this.menubar = menubar;

    startServer.addActionListener(this);
    stopServer.addActionListener(this);
    testConnect.addActionListener(this);

    this.add(startServer);
    this.add(stopServer);
    this.add(separator1);
    this.add(testConnect);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    final var src = e.getSource();
    if (src == startServer) {
      if (server == null) {
        server = new ShareServer("", -1);
        if (server.init()) {
          server.start();
        }
      }
    }
    else if (src == stopServer) {
        if (server != null) {
          server.stop();
          server = null;
        }
    }
    else if (src == testConnect) {
      ShareClient client = new ShareClient(null, -1);
      client.connect();
      client.send("Hi");
    }
  }

  public void localeChanged() {
    this.setText(S.get("networkMenu"));
    startServer.setText(S.get("networkStartServerItem"));
    stopServer.setText(S.get("networkStopServerItem"));
    testConnect.setText(S.get("networkTestConnectItem"));
  }
}
