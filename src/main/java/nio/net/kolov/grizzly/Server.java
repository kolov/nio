package nio.net.kolov.grizzly;

/*
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2007-2008 Sun Microsystems, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 *
 * Contributor(s):
 *
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 *
 */

import com.sun.grizzly.*;
import com.sun.grizzly.filter.ReadFilter;

/**
 * Simple EchoServer using grizzly, copied from somewhere on grizzly.java.net
 */
public class Server {


    final TCPSelectorHandler tcp_handler = new TCPSelectorHandler();
    final Controller controller = new Controller();

    public static void main(String[] args) {
        int port = 0;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                if (args[++i] != null) {
                    port = Integer.parseInt(args[i]);
                    System.out.println("Port set to " + port);
                } else {
                    System.out.println("No port set!");
                }
            }
        }
        Server s = new Server();
        s.execute(port);
    }

    void execute(int pPort) {
        if (pPort != 0) {
            tcp_handler.setPort(pPort);
        } else {
            System.out.println("No port given!");
            System.exit(1);
        }
        controller.setSelectorHandler(tcp_handler);
        controller.setProtocolChainInstanceHandler(
                new DefaultProtocolChainInstanceHandler() {
                    public ProtocolChain poll() {
                        ProtocolChain protocol_chain = protocolChains.poll();
                        if (protocol_chain == null) {
                            protocol_chain = new DefaultProtocolChain();
                            protocol_chain.addFilter(new ReadFilter());
                            protocol_chain.addFilter(new ResponseFilter());
                        }
                        return protocol_chain;
                    }
                }); // end overridden method


        try {
            controller.start();
        } catch (Exception e) {
            System.out.println("Exception in controller...");
        }
    }
}
