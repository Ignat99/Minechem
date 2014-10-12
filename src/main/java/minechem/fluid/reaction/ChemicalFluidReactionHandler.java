package minechem.fluid.reaction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import minechem.MinechemItemsRegistration;
import minechem.Settings;
import minechem.fluid.FluidChemical;
import minechem.fluid.FluidChemicalDispenser;
import minechem.fluid.FluidElement;
import minechem.fluid.FluidHelper;
import minechem.item.MinechemChemicalType;
import minechem.item.element.ElementEnum;
import minechem.item.element.ElementItem;
import minechem.item.molecule.MoleculeEnum;
import minechem.item.molecule.MoleculeItem;
import minechem.utils.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class ChemicalFluidReactionHandler
{
	public static final Map<ChemicalFluidReactionRule, ChemicalFluidReactionOutput> reactionRules=new HashMap<ChemicalFluidReactionRule, ChemicalFluidReactionOutput>();
	
	private static final Random ran=new Random();
	
    @SubscribeEvent
    public void tick(TickEvent.WorldTickEvent event)
    {
    	if (!Settings.reactionItemMeetFluid){
    		return;
    	}
    	
        World world=event.world;
        
        for (Object p : world.playerEntities){
            EntityPlayer player=(EntityPlayer) p;
            double rangeToCheck = 32.0D;
            
            List<EntityItem> itemList = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(rangeToCheck, rangeToCheck, rangeToCheck));
            for (EntityItem entityItem : itemList)
            {
            	ItemStack itemStack=entityItem.getEntityItem();
            	Item item=itemStack.getItem();
            	MinechemChemicalType chemicalA=null;
            	if (item==MinechemItemsRegistration.element){
            		chemicalA=ElementItem.getElement(itemStack);
            	}else if (item==MinechemItemsRegistration.molecule){
            		chemicalA=MoleculeItem.getMolecule(itemStack);
            	}
            	
            	if (chemicalA!=null&&world.isMaterialInBB(entityItem.boundingBox, Material.water)){
            		int x=MathHelper.floor_double(entityItem.posX);
            		int y=MathHelper.floor_double(entityItem.posY);
            		int z=MathHelper.floor_double(entityItem.posZ);
            		Block block=world.getBlock(x, y, z);
            		MinechemChemicalType chemicalB=getChemical(block);
            			
            		if (chemicalB!=null){
            			ChemicalFluidReactionRule rule=new ChemicalFluidReactionRule(chemicalA, chemicalB);
            			if (reactionRules.containsKey(rule)){
            				explosionReaction(world,entityItem,x,y,z,rule,!(FluidChemicalDispenser.canDrain(world,block,x,y,z)));
            				itemStack.stackSize--;
            				if (itemStack.stackSize<=0){
            					world.removeEntity(entityItem);
            				}else{
            					entityItem.setEntityItemStack(itemStack);
            				}
            			}
            		}
            		
            	}
            }
        }   
    }

    public static void initExplodableChemical(){
    	// TODO Add more reaction rules -yushijinhun
    	Map<MinechemChemicalType, Float> map;
    	
    	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.lithiumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.Li),new ChemicalFluidReactionOutput(map, 0.1f));
    	
    	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.sodiumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.Na),new ChemicalFluidReactionOutput(map, 0.15f));
    	
    	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.potassiumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.K),new ChemicalFluidReactionOutput(map, 0.2f));
    	
    	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.rubidiumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.Rb),new ChemicalFluidReactionOutput(map, 0.25f));

       	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.cesiumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.Cs),new ChemicalFluidReactionOutput(map, 0.3f));
    	
       	map=new HashMap<MinechemChemicalType, Float>();
    	map.put(ElementEnum.H, 1f);
    	map.put(MoleculeEnum.franciumHydroxide, 1f);
    	reactionRules.put(new ChemicalFluidReactionRule(MoleculeEnum.water, ElementEnum.Fr),new ChemicalFluidReactionOutput(map, 0.4f));

//    	
//    	explosionReactionRules.add(new ChemicalFluidReactionRule(MoleculeEnum.water, MoleculeEnum.sulfuricAcid));
//    	explosionReactionRules.add(new ChemicalFluidReactionRule(MoleculeEnum.water, MoleculeEnum.calciumOxide));
//    	explosionReactionRules.add(new ChemicalFluidReactionRule(MoleculeEnum.water, MoleculeEnum.potassiumOxide));
//    	explosionReactionRules.add(new ChemicalFluidReactionRule(MoleculeEnum.water, MoleculeEnum.sodiumOxide));
    	
    }
    
    private static void explosionReaction(World world,Entity entity,int x,int y,int z, ChemicalFluidReactionRule rule,boolean popFlowingFluid){
    	ChemicalFluidReactionOutput output=reactionRules.get(rule);
    	if (output==null){
    		return;
    	}
    	
    	if (output.explosionLevel!=Float.NaN){
    		world.createExplosion(null, x, y, z, output.explosionLevel, true);
    	}
    	
    	int foundVectors=0;
    	
    	/*
    	 * 0:y-1
    	 * 1:y
    	 * 2:y+1
    	 */
    	Vector[] availableSpaces=new Vector[3];
    	for (int i=0;i<availableSpaces.length;i++){
    		availableSpaces[i]=findAvailableSpacesAtCrossSection(world, x, y-1+i, z, 1);
    		foundVectors+=availableSpaces[i].size();
    	}
    	
    	int needVectors=output.outputs.size();
    	
    	Iterator<MinechemChemicalType> it=output.outputs.keySet().iterator();
    	while(it.hasNext()&&needVectors>0&&foundVectors>0){
    	MinechemChemicalType chemical=it.next();
    		
    		boolean isGas=chemical.roomState().isGas();
    		
    		if (ran.nextFloat()<=output.outputs.get(chemical)){
    			Vector3 vector=null;
    			
    			if (isGas){
    				for (int i=availableSpaces.length-1;i>-1;i--){
    					if (!availableSpaces[i].isEmpty()){
    						vector=(Vector3) availableSpaces[i].remove(availableSpaces[i].size()-1);
    						break;
    					}
    				}
    			}else{
    				for (int i=0;i<availableSpaces.length;i++){
    					if (!availableSpaces[i].isEmpty()){
    						vector=(Vector3) availableSpaces[i].remove(availableSpaces[i].size()-1);
    						break;
    					}
    				}
    			}
    			
    			if (vector==null){
    				ItemStack itemStack=FluidChemicalDispenser.createItemStack(chemical, 1);
    				FluidChemicalDispenser.throwItemStack(world, itemStack, x, y, z);
    			}else{
    				int px=vector.intX();
    				int py=vector.intY();
    				int pz=vector.intZ();
    				
    				world.func_147480_a(px, py, pz, true);
    				world.setBlockToAir(px, py, pz);
    				
    				Block fluidBlock=null;
    				if (chemical instanceof ElementEnum){
    					fluidBlock=FluidHelper.elementsBlocks.get(FluidHelper.elements.get(chemical));
    				}else if (chemical instanceof MoleculeEnum){
    					fluidBlock=FluidHelper.moleculeBlocks.get(FluidHelper.molecule.get(chemical));
    				}
    				
    				if (fluidBlock!=null){
    					world.setBlock(px, py, pz, fluidBlock, popFlowingFluid?1:0, 3);
    				}
    			}
    		}
    	}
    }

    public static MinechemChemicalType getChemical(Block block){
    	MinechemChemicalType chemical=null;
		if (block instanceof IFluidBlock){
			Fluid fluid=((IFluidBlock)block).getFluid();
			if (fluid instanceof FluidElement){
				chemical=((FluidElement)fluid).element;
			}else if(fluid instanceof FluidChemical){
				chemical=((FluidChemical)fluid).molecule;
			}else if(fluid==FluidRegistry.WATER){
				chemical=MoleculeEnum.water;
			}
		}else if (block==Blocks.water||block==Blocks.flowing_water){
			chemical=MoleculeEnum.water;
		}
		
		return chemical;
    }
    
    public static boolean checkToExplode(Block source,Block destination,World world,int destinationX,int destinationY,int destinationZ,int sourceX,int sourceY,int sourceZ){
    	MinechemChemicalType chemicalA=getChemical(source);
    	MinechemChemicalType chemicalB=getChemical(destination);
    	if (chemicalA!=null&&chemicalB!=null){
    		ChemicalFluidReactionRule rule=new ChemicalFluidReactionRule(chemicalA, chemicalB);
    		
    		if (reactionRules.containsKey(rule)){
    			boolean flag=!(FluidChemicalDispenser.canDrain(world, source, sourceX, sourceY, sourceZ)&&FluidChemicalDispenser.canDrain(world, destination, destinationX, destinationY, destinationZ));
    			world.setBlockToAir(sourceX, sourceY, sourceZ);
    			world.setBlockToAir(destinationX, destinationY, destinationZ);
	    		explosionReaction(world,null,destinationX,destinationY,destinationZ,rule,flag);
	    		return true;
    		}
    	}
    	
    	return false;
    }
    
    public static Vector<Vector3> findAvailableSpacesAtCrossSection(World world,int centerX,int centerY,int centerZ,int size){
    	Vector<Vector3> spaces=new Vector<Vector3>();
    	for (int xOffset=-size;xOffset<=size;xOffset++){
    		for (int zOffset=-size;zOffset<=size;zOffset++){
    			int x=centerX+xOffset;
    			int z=centerZ+zOffset;
    			
    			if (world.isAirBlock(x, centerY, z)||!world.getBlock(x, centerY, z).getMaterial().isSolid()){
    				spaces.add(new Vector3(x,centerY,z));
    			}
    		}
    	}
    	
    	return spaces;
    }
}
