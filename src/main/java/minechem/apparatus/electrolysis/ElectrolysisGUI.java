package minechem.apparatus.electrolysis;

import minechem.Compendium;
import minechem.apparatus.prefab.gui.container.BasicGuiContainer;
import minechem.apparatus.prefab.gui.element.GuiArrow;
import minechem.apparatus.prefab.gui.element.GuiEnergybar;
import minechem.apparatus.prefab.gui.element.GuiFluidTank;
import net.minecraft.entity.player.InventoryPlayer;

public class ElectrolysisGUI extends BasicGuiContainer {
    private static final int arrowPosX = 74, arrowPosY = 33;

    public ElectrolysisGUI(InventoryPlayer inventoryPlayer, ElectrolysisTileEntity electrolysis) {
        super(new ElectrolysisContainer(inventoryPlayer, electrolysis));
        this.name = electrolysis.getName();
        this.texture = Compendium.Resource.GUI.electrolysis;
        addGuiElement(new GuiEnergybar(electrolysis.getEnergy(), 12, 23));
        addGuiElement(new GuiFluidTank(electrolysis.getTank(), 32, 23));
        addGuiElement(new GuiArrow(arrowPosX - 1, arrowPosY - 1, electrolysis::getProgression));
    }
}
