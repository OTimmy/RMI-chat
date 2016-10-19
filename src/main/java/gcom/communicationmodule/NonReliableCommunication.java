package gcom.communicationmodule;

import gcom.groupmodule.Member;
import gcom.message.*;
import gcom.status.GCOMException;
import jdk.internal.org.objectweb.asm.Handle;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by c12ton on 2016-09-30.
 * An unreliable communication implementation,
 * it does not care for if message has been received or not.
 */
public class NonReliableCommunication extends Communication{

    public NonReliableCommunication()  {
        inMessages = new LinkedBlockingDeque<>();
    }

    @Override
    public void putMessage(Message m) {
        inMessages.add(m);
    }

    /**
     * Non reliable multi cast.
     * @param members
     * @param message
     * @throws RemoteException
     * @throws NotBoundException
     * @throws InterruptedException
     */
    @Override
    public void sendMessage(Member[] members, Message message)  {



        for(Member m:members) {
            try {

                message.setToName(m.getName());
                Message cloneMessage = (Message) message.clone();

//                Message copy = cloneMessage(clo);
                m.sendMessage(cloneMessage);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }


//    private Message cloneMessage(Message m) {
//        try {
//            String fromName = m.getFromName();
//            String toName  = m.getToName();
//            String groupName = m.getGroupName();
//            HashMap<String,Integer> vecor = m.getVectorClock();
//
//            Message cloneMessage = null;
//
//            switch(m.getMessageType()) {
//
//                case CHAT_MESSAGE:
//                    Chat chat = (Chat) m;
//                    String message = chat.getMessage();
//                    cloneMessage = new  ChatMessage(fromName,message);
//                    break;
//                case LEAVE_MESSAGE:
//                    Leave leave = (Leave) m;
//                    cloneMessage = new LeaveMessage(leave.getName());
//                    break;
//                case JOIN_MESSAGE:
//                    Join join = (Join) m;
//                    cloneMessage = new JoinMessage(((Join) m).getMember());
//                    break;
//                case ELECTION_MESSAGE:
//                    Election election = (Election) m;
//                    cloneMessage = new ElectionMessage(election.getLeader());
//                    break;
//                case DELETE_MESSAGE:
//                    Delete delete = (Delete) m;
//                    break;
//            }
//
//            cloneMessage.setVectorClock(vecor);
//            cloneMessage.setFromName(fromName);
//            cloneMessage.setGroupName(groupName);
//            cloneMessage.setToName(toName);
//            return  cloneMessage;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
