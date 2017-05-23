package minechem.apparatus.prefab.block;

import minechem.Compendium;
import minechem.registry.CreativeTabRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/*
 * Extendable class for simple non-container blocks
 */
public abstract class BasicBlock extends Block
{

    /**
     * Unnamed blocks are given a default name
     */
    public BasicBlock()
    {
        this(Compendium.Naming.name + " Basic Block");
    }

    /**
     * Create a basic block with a given name
     *
     * @param blockName unlocalized name of the block
     */
    public BasicBlock(String blockName)
    {
        this(blockName, Material.GRASS, SoundType.GROUND);
    }


    public BasicBlock(String blockName, Material material, SoundType soundType)
    {
        super(material);
        this.setRegistryName(blockName);
        this.setSoundType(soundType);
        setCreativeTab(CreativeTabRegistry.TAB_PRIMARY);
        //textureName = Compendium.Naming.id + ":" + blockName + "Icon";
    }
}
