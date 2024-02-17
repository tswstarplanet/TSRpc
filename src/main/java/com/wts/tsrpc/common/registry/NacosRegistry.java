package com.wts.tsrpc.common.registry;

import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.wts.tsrpc.client.Endpoint;
import com.wts.tsrpc.common.utils.CollectionUtils;
import com.wts.tsrpc.exception.BizException;
import com.wts.tsrpc.server.manage.Application;

import java.util.List;
import java.util.Properties;

public class NacosRegistry extends AbstractRegistry implements Registry {

    private final NamingService namingService;

    public NacosRegistry(String serverList, String namespace) {
        super(serverList, namespace);
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.NAMESPACE, namespace);
        properties.setProperty(PropertyKeyConst.SERVER_ADDR, serverList);
        try {
            this.namingService = NamingFactory.createNamingService(properties);
        } catch (NacosException e) {
            throw new BizException(STR."Create nacos naming service error: [\{e.getErrCode()} : \{e.getErrMsg()}]");
        }
    }

    @Override
    public void register(Application application, Endpoint endpoint) {
        try {
            namingService.registerInstance(application.getKey(), endpoint.getHost(), endpoint.getPort());
        } catch (NacosException e) {
            throw new BizException(STR."Register application instance error: \{e.getErrCode()}, \{e.getErrMsg()}");
        }
    }

    @Override
    public void deregister(Application application, Endpoint endpoint) {
        try {
            namingService.deregisterInstance(application.getKey(), endpoint.getHost(), endpoint.getPort());
        } catch (NacosException e) {
            throw new BizException(STR."Deregister application instance error: \{e.getErrCode()}, \{e.getErrMsg()}");
        }
    }

    @Override
    public void subscribe(Application application) {
        try {
            namingService.subscribe(application.getKey(), event -> {
                if (event instanceof NamingEvent) {
                    if (CollectionUtils.isEmpty(((NamingEvent) event).getInstances())) {
                        super.getInstanceMap().get(((NamingEvent) event).getServiceName()).clear();
                    } else {
                        List<ApplicationInstance> applicationInstances = ((NamingEvent) event).getInstances().stream().map(instance -> {
                            String[] keyArray = instance.getServiceName().split(",");
                            return new ApplicationInstance(keyArray[0], keyArray[1], instance.getIp(), instance.getPort());
                        }).toList();
                        super.getInstanceMap().put(((NamingEvent) event).getServiceName(), applicationInstances);
                    }
                }
            });
        } catch (NacosException e) {
            throw new BizException(STR."Subscribe application instance error: \{e.getErrCode()}, \{e.getErrMsg()}");
        }
    }
}
