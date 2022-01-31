package net.bdew.lib.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("CanBeFinal")
public class Capabilities {
    public static Capability<IItemHandler> CAP_ITEM_HANDLER = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<IFluidHandler> CAP_FLUID_HANDLER = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<IFluidHandlerItem> CAP_FLUID_HANDLER_ITEM = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<IEnergyStorage> CAP_ENERGY_HANDLER = CapabilityManager.get(new CapabilityToken<>() {
    });
}
