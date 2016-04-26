package greglife.block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GregLife;
import greglife.tileentity.TileTimeTorch;
import greglife.util.IconDic;
import greglife.util.Utils;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockTimeTorch extends BlockTorch{

	public static HashMap<String, int[]> upgrades = new HashMap();

	static
	{
		upgrades.put("Heat", new int[] { 16, 1 });
		upgrades.put("Healing", new int[] { 17, 6 });
		upgrades.put("Bane", new int[] { 18, 14 });
		upgrades.put("Camo", new int[] { 19, 0 });
		upgrades.put("Repair", new int[] { 20, 4 });
		upgrades.put("Time", new int[] { 21, 12 });
		upgrades.put("Build", new int[] { 22, 3 });
	}

	public BlockTimeTorch(String name){
		this.setBlockName(name);
		this.setCreativeTab(GregLife.GLTab);
		this.setHardness(1.0F);
		this.setResistance(2000.0F);
	}

	public int getLightValue(IBlockAccess world, int x, int y, int z){
		return 15;
	}

	public boolean hasTileEntity(int meta){
		return true;
	}

	public TileEntity createTileEntity(World world, int meta){
		return new TileTimeTorch();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
	  {
	    super.onBlockPlacedBy(world, x, y, z, entity, stack);

	    NBTTagCompound upgrades = new NBTTagCompound();
	    int color = stack.getItemDamage();

	    TileEntity tile = world.getTileEntity(x, y, z);
	    if ((tile instanceof TileTimeTorch))
	    {
	      try
	      {
	        for (String name : this.upgrades.keySet())
	        {
	          int[] ints = (int[])this.upgrades.get(name);
	          if (stack.getItemDamage() == ints[0])
	          {
	            color = ints[1];
	            upgrades.setInteger(name, 1);
	            break;
	          }
	        }
	      }
	      catch (Exception e) {}
	      TileTimeTorch torch = (TileTimeTorch)tile;
	      torch.upgrades = upgrades;
	      //torch.color = ((byte)color);
	    }
	  }

	public void onBlockActivated(World world, int x, int y, int z, int par5, int meta) //???????????
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if ((tile instanceof TileTimeTorch))
		{
			TileTimeTorch torch = (TileTimeTorch)tile;

			ItemStack stack = new ItemStack(this.getBlockFromItem(null), 1, 0);
			if ((torch.upgrades != null) && (!torch.upgrades.hasNoTags())) {
				try
				{
					int damage = -1;
					for (String key : upgrades.keySet()) {
						if (torch.upgrades.hasKey(key))
						{
							damage = ((int[])upgrades.get(key))[0];
							break;
						}
					}
					if (damage > 0) {
						stack.setItemDamage(damage);
					}
				}
				catch (Exception e) {}
			}
			Utils.dropItem(stack, world, x, y, z);
			world.setBlockToAir(x, y, z);
		}
	}

	public boolean rotateBlock(World world, int x, int y, int z, ForgeDirection axis){
		return false;
	}

	public ArrayList getBlockDropped(World world, int x, int y, int z, int metadata, int fortune){
		return new ArrayList();
	}

	public IIcon func_71895_b(IBlockAccess world, int x, int y, int z, int side){
		try{
			return IconDic.torch;
		}catch(Exception e){
			e.printStackTrace();
		}
		return this.blockIcon;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		//for (int i = 0; i < 20; i++) {
		//	list.add(new ItemStack(this, 1, i));
		//}
		//if (Config.timeUpgradeTorch) {
		list.add(new ItemStack(this, 1, 21));
		//}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister reggie){
		//for(int i = 0; i < 16; i++){
			IconDic.torch = reggie.registerIcon("greglife:timeTorch");// +i
		//}
		this.blockIcon = IconDic.torch;
	}

	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand){
		try{
			TileTimeTorch torch = (TileTimeTorch)world.getTileEntity(x, y, z);
			if(torch.upgrades.hasKey("Camo")){
				return;
			}
		}
		catch (Exception e){}
		super.randomDisplayTick(world, x, y, z, rand);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9){
		try{
			TileEntity tile = world.getTileEntity(x, y, z);
			if((tile != null) && ((tile instanceof TileTimeTorch))){
				TileTimeTorch torch = (TileTimeTorch)tile;
				//if((torch.upgrades.hasKey("Time")) && (Config.timeUpgradeTorch)){
					//if(Proxies.common.isSimulating(world)){
						torch.timeType += 1;
						if (torch.timeType > 4) {
							torch.timeType = 0;
						}
						String message = "Time mode: ";
						switch (torch.timeType){
						case 0:
						default:
							message = message + "None";
							break;
						case 3:
							message = message + "Fast";
							break;
						case 4:
							message = message + "Hyper";
							break;
						case 2:
							message = message + "Slow";
							break;
						case 1:
							message = message + "Stop";
						}
						//Proxies.common.sendChatToPlayer(player, message);
						if(world.isRemote){
							player.addChatComponentMessage(new ChatComponentText(message));
						}
					//}
					return true;
				//}
			}
		}
		catch (Exception e)
		{
			//DebugUtils.printError(e);
			System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
		}
		return false;
	}
}
