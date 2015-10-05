/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jgroups.presentation.chat;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.ReplicatedHashMap;
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
public class Demo6_SimpleChatClientWithReplicatedHashMap extends ReceiverAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONFIG_PATH = "udp.xml";
    private static final String CLUSTER_NAME = "ChatDemo";

    private JChannel channel;

    private ReplicatedHashMap<String, Integer> wordCounter;

    public static void main(String[] args) throws Exception {
        new Demo6_SimpleChatClientWithReplicatedHashMap().runClient();
    }

    @Override
    public void viewAccepted(View view) {
        logger.info("New View :: {}", view);
    }

    private void runClient() throws Exception {
        channel = new JChannel(MethodHandles.lookup().lookupClass().getClassLoader().getResource(CONFIG_PATH));
        wordCounter = new ReplicatedHashMap<>(channel);
        channel.connect(CLUSTER_NAME);
        wordCounter.start(10_000);

        while (true) {
            try {
                String input = getTextFromConsole();
                switch (input) {
                    case "count":
                        logger.info("word counter {}", wordCounter);
                        break;
                    default:
                        printOut(input);
                        break;
                }
            } catch (ApplicationExitException e) {
                logger.info("Bye!");
                channel.close();
                break;
            }
        }
    }

    @SuppressWarnings("unused")
    public void printOut(String text) {
        logger.info("New Message :: {}", text);
        Integer valueFromTheCache = wordCounter.get(text);
        int currentCount = valueFromTheCache == null ? 0 : valueFromTheCache;
        wordCounter.put(text, ++currentCount);
    }
}
