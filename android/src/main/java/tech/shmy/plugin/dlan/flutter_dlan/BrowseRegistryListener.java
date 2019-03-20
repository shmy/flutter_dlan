package tech.shmy.plugin.dlan.flutter_dlan;

import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ServiceType;
import org.fourthline.cling.model.types.UDAServiceType;
import org.fourthline.cling.registry.DefaultRegistryListener;
import org.fourthline.cling.registry.Registry;

import java.util.ArrayList;
import java.util.HashMap;

import io.flutter.plugin.common.EventChannel.EventSink;

public class BrowseRegistryListener extends DefaultRegistryListener {
    private ArrayList<Device> mDeviceList = new ArrayList<>();
    private ArrayList<HashMap> flutterDeviceList = new ArrayList<>();
    private static final ServiceType AV_TRANSPORT_SERVICE = new UDAServiceType("AVTransport");
    public static EventSink eventSink;
    Service getDeviceForUuid(String uuid) {
        int i = 0;
        for (; i < mDeviceList.size(); i ++) {
            String currentUuid = mDeviceList.get(i).getIdentity().getUdn().getIdentifierString();
            if (currentUuid.equals(uuid)) {
                break;
            }
        }
        System.out.println(mDeviceList.get(i));
        return mDeviceList.get(i).findService(AV_TRANSPORT_SERVICE);
    }
    ArrayList<HashMap> getDevices() {
        return this.flutterDeviceList;
    }
    @Override
    public void deviceAdded(Registry registry, Device device) {
        HashMap fd = new HashMap();
        fd.put("name", device.getDetails().getFriendlyName());
        fd.put("display", device.getDisplayString());
        fd.put("uuid", device.getIdentity().getUdn().getIdentifierString());
        if (flutterDeviceList.indexOf(fd) != -1) {
            return;
        }
        super.deviceAdded(registry, device);
        mDeviceList.add(device);
        flutterDeviceList.add(fd);
        System.out.println("-----get device-------");
        System.out.println(device);
        System.out.println(mDeviceList);
        if (BrowseRegistryListener.eventSink != null) {
            BrowseRegistryListener.eventSink.success(flutterDeviceList);
        }
    }

    @Override
    public void deviceRemoved(Registry registry, Device device) {
        super.deviceRemoved(registry, device);
        mDeviceList.remove(device);
    }
}
