package greglife.block.basic;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import greglife.GLContent;
import greglife.GregLife;
import greglife.util.FXutil;
import greglife.util.IconDic;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BlockGregLifeOre extends Block{

	public BlockGregLifeOre(String name){
		super(Material.rock);
		this.setStepSound(soundTypeStone);
		this.setBlockName(name);
		this.setHardness(3.0F);
		this.setResistance(10.0F);
		this.setHarvestLevel("pickaxe", 1);
		this.setCreativeTab(GregLife.GLTab);

		//this.setBlockTextureName("greglife:oreTop");
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z){
		Minecraft mc = Minecraft.getMinecraft();

		if(mc.thePlayer.getHeldItem().getItem().getDamage(mc.thePlayer.getHeldItem()) == 1){
			world.setBlockMetadataWithNotify(x, y, z, 1, 0);
		}else{
			world.setBlockMetadataWithNotify(x, y, z, 0, 0);
		}
	}

	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconregister){
		IconDic.stone = iconregister.registerIcon("greglife:stone");
		IconDic.nether = iconregister.registerIcon("greglife:nether");
		IconDic.top = iconregister.registerIcon("greglife:oreTop");

		this.blockIcon = IconDic.top;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta){
		if(meta == 1){
			return IconDic.nether;
		}
		return IconDic.stone;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
	}

	@Override
	public Item getItemDropped(int meta, Random random, int fortune) {
		return GLContent.itemGregLifeGem;
	}

	public int dropAmount(Random rand){
		return rand.nextInt(3) + 2;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random rand){
		return dropAmount(rand);
	}

	@Override
	public int quantityDroppedWithBonus(int bonus, Random rand){
		int drop = 0;
	    if (bonus > 0){
	      int temp = rand.nextInt(bonus + 2);
	      drop = dropAmount(rand) + temp;
	    }else{
	      drop = dropAmount(rand);
	    }
	    return drop;
	}

	@Override
	public void dropXpOnBlockBreak(World world, int x, int y, int z, int exp){
		exp = world.rand.nextInt(3) + 2;
		while (exp > 0){
			int amt = EntityXPOrb.getXPSplit(exp);
			exp -= amt;
			world.spawnEntityInWorld(new EntityXPOrb(world, x + 0.5D, y + 0.5D, z + 0.5D, amt));
		}
	}

	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata){
		return false;
	}

	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer renderer){
		FXutil.makeShiny(world, x, y, z, 2, 16776960, 32, true);
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer renderer){
		if((world != null) && (target != null)){
			FXutil.makeShiny(world, target.blockX, target.blockY, target.blockZ, 2, 16776960, 3, true);
		}
		return true;
	}
}
