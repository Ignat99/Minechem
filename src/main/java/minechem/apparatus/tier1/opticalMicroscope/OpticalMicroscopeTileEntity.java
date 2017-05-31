package minechem.apparatus.tier1.opticalMicroscope;

import minechem.Compendium;
import minechem.apparatus.prefab.tileEntity.BaseTileEntity;
import minechem.apparatus.prefab.tileEntity.storageTypes.BasicInventory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class OpticalMicroscopeTileEntity extends BaseTileEntity
{
    private BasicInventory inventory;

    public OpticalMicroscopeTileEntity()
    {
        super(Compendium.Naming.opticalMicroscope);
        this.inventory = new BasicInventory(1, getName());
        attachCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, new InvWrapper(inventory));
    }

    public BasicInventory getInventory() {
        return inventory;
    }
}
