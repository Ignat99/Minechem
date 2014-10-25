package minechem.tileentity.leadedchest;

import minechem.gui.GuiContainerTabbed;
import minechem.gui.GuiTabHelp;
import minechem.reference.Reference;
import minechem.reference.Textures;
import minechem.utils.MinechemHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class LeadedChestGui extends GuiContainerTabbed
{

	private static final ResourceLocation resourceLocationLeadedChestGUI = new ResourceLocation(Reference.ID, Textures.LEADED_CHEST_GUI);
	int guiWidth = 176;
	int guiHeight = 217;
	LeadedChestTileEntity leadedchest;

	public LeadedChestGui(InventoryPlayer inventoryPlayer, LeadedChestTileEntity leadedChest)
	{
		super(new LeadedChestContainer(inventoryPlayer, leadedChest));
		this.leadedchest = leadedChest;
		addTab(new GuiTabHelp(this, MinechemHelper.getLocalString("help.leadChest")));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2)
	{
		super.drawGuiContainerForegroundLayer(par1, par2);
		String info = MinechemHelper.getLocalString("gui.title.leadedchest");
		int infoWidth = this.fontRendererObj.getStringWidth(info);
		this.fontRendererObj.drawString(info, (guiWidth - infoWidth) / 2, 5, 0x000000);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.mc.renderEngine.bindTexture(resourceLocationLeadedChestGUI);

		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		GL11.glPushMatrix();
		GL11.glTranslatef(x, y, 0.0F);
		GL11.glPopMatrix();
	}

}
