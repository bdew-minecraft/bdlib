package net.bdew.lib.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.IItemHandler;

@SuppressWarnings("CanBeFinal")
public class Capabilities {
    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> CAP_ITEM_HANDLER = null;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> CAP_FLUID_HANDLER = null;

    @CapabilityInject(IFluidHandlerItem.class)
    public static Capability<IFluidHandlerItem> CAP_FLUID_HANDLER_ITEM = null;

    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> CAP_ENERGY_HANDLER = null;
}
