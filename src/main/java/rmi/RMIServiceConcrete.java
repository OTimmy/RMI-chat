package rmi;

import rmi.debugservice.DebugService;
import rmi.debugservice.DebugServiceConcrete;
import rmi.nameservice.NameService;
import rmi.nameservice.NameServiceConcrete;

import java.rmi.RemoteException;

/**
 * Created by c12ton on 10/15/16.
 */
public class RMIServiceConcrete implements RMIService {
    private DebugServiceConcrete debugService;
    private NameServiceConcrete nameService;

    public RMIServiceConcrete() throws RemoteException {
        debugService        = new DebugServiceConcrete();
        nameService         = new NameServiceConcrete();
    }


    @Override
    public NameService getNameService() throws RemoteException {
        return nameService;
    }

    @Override
    public DebugService getDebugService() throws RemoteException {
        return debugService;
    }
}
