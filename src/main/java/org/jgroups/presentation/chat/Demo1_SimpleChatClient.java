/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jgroups.presentation.chat;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.presentation.chat.utils.ApplicationExitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static org.jgroups.presentation.chat.utils.ConsoleHelper.getTextFromConsole;

/**
 * Before running this demo, please make sure:
 * <ul>
 * <li>-Djava.net.preferIPv4Stack=true</li>
 * </ul>
 */
public class Demo1_SimpleChatClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONFIG_PATH = "config.xml";
    private static final String CLUSTER_NAME = "ChatDemo";

    private JChannel channel;

    public static void main(String[] args) throws Exception {
        new Demo1_SimpleChatClient().runClient();
    }

    private void runClient() throws Exception {
        channel = new JChannel(MethodHandles.lookup().lookupClass().getClassLoader().getResource(CONFIG_PATH));

        channel.setReceiver(new ReceiverAdapter() {
            @Override
            public void receive(Message msg) {
                logger.info("New Message :: [{}] -> [{}] :: {}", msg.src(), msg.dest(), msg.getObject());
            }
        });

        channel.connect(CLUSTER_NAME);

        while (true) {
            try {
                String input = getTextFromConsole();
                Message msg = new Message(null, null, input);
                channel.send(msg);
            } catch (ApplicationExitException e) {
                logger.info("Bye!");
                channel.close();
                break;
            }
        }
    }
}
