package greglife.item.tool;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import greglife.GregLife;
import greglife.util.GLTextHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemCosmosPick extends ItemTool{

	public static final int[] ToolLevelExp = { 20, 84, 340, 1364, 5460, 21844, 87380, 349524, 1398100 };
	public static final float[] digStrength = { 2.0F, 3.0F, 4.0F, 5.0F, 6.0F, 7.0F, 9.0F, 11.0F, 13.0F, 1500.0F };

	public static final String tagname = "seitiattribute";
	public static final Block[] blocksEffectiveAgainst = new Block[] {Blocks.stone, Blocks.cobblestone, Blocks.mossy_cobblestone, Blocks.cobblestone_wall, Blocks.furnace, Blocks.log, Blocks.planks, Blocks.crafting_table, Blocks.dirt, Blocks.grass};
	private static final Set canHarvestBlock = Sets.newHashSet(new Block[] {Blocks.stone, Blocks.cobblestone, Blocks.mossy_cobblestone, Blocks.cobblestone_wall, Blocks.furnace, Blocks.log, Blocks.planks, Blocks.crafting_table});
	  protected PickaxeDummy dummy = new PickaxeDummy();

	protected int exp;
	protected int maxExp;
	protected int level;
	protected int maxLevel;

	public ItemCosmosPick(String name)
	{
		super(1.0F, ToolMaterial.WOOD, canHarvestBlock);
		setTextureName("greglife:cosmos_pick");
		this.setCreativeTab(GregLife.GLTab);
		setUnlocalizedName(name);
		this.maxStackSize = 1;
		setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack stack){
		return (EnumChatFormatting.ITALIC+GLTextHelper.makeSANIC("Pickaxe of Cosmos"));
	}

	public void onCreated(ItemStack item, World world, EntityPlayer player)
	  {
	    ensureTagCompound(item);
	  }

	  @SuppressWarnings({ "unchecked", "rawtypes" })
	public void addInformation(ItemStack item, EntityPlayer player, List list, boolean bool)
	  {
	    if (!checkHasSeitiTag(item))
	    {
	      list.add("A Mighty Tool With Infinite Durability");
	      list.add("And Much More!");
	      return;
	    }
	    ensureTagCompound(item);
	    int level = getSeitiTagCompoundContentInt(item, "level");
	    int exp = getSeitiTagCompoundContentInt(item, "exp");
	    int next = 0;
	    if (level < 10) {
	      next = ToolLevelExp[(level - 1)];
	    }
	    list.add("Level:  " + level);
	    String str;
	    if (level != 10) {
	      str = "XP: " + exp + " / " + next;
	    } else {
	      str = "XP: " + exp;
	    }
	    list.add(str);
	  }

	  public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player){
	    return item;
	  }

	  public boolean func_77660_a(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity)
	  {
	    if (!world.isRemote)
	    {
	      ensureTagCompound(stack);
	      Block block1 = world.getBlock(x, y, z);
	      boolean fits = false;
	      for (int i = 0; i != this.canHarvestBlock.size(); i++) {
	        if (canHarvestBlock == block)
	        {
	          fits = true;
	          break;
	        }
	      }
	      if (!fits) {
	        fits = (block.getMaterial() == Material.rock) || (block.getMaterial() == Material.sand) || (block.getMaterial() == Material.wood);
	      }
	      if (!fits) {
	        fits = block == Blocks.stone;
	      }
	      fits = true;
	      if (fits)
	      {
	        int curexp = getSeitiTagCompoundContentInt(stack, "exp");
	        int curlev = getSeitiTagCompoundContentInt(stack, "level");

	        boolean debug = true; //TODO

	        if (debug) {
	          curexp += 10000;
	        } else {
	          curexp++;
	        }
	        if ((curlev != 10) &&
	          (curexp >= ToolLevelExp[(curlev - 1)])) {
	          curlev++;
	        }
	        setSeitiTagCompoundContentInt(stack, "exp", Integer.valueOf(curexp), Integer.valueOf(0));
	        setSeitiTagCompoundContentInt(stack, "level", Integer.valueOf(curlev), Integer.valueOf(0));
	      }
	    }
	    return false;
	  }

	  @Override
	  public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
		  this.func_77660_a(stack, world, block, x, y, z, entity);

		  if(!stack.isItemEnchanted()){
			  int level = getSeitiTagCompoundContentInt(stack, "level");
			  System.out.println("isItemEnchanted");
			  if(level == 10){
				  stack.addEnchantment(Enchantment.efficiency, 100);
				  stack.addEnchantment(Enchantment.fortune, 100);
			  }
		  }
		  return super.onBlockDestroyed(stack, world, block, x, y, z, entity);
	  }

	  public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block, int meta)
	  {
		  ensureTagCompound(par1ItemStack);
		  int level = getSeitiTagCompoundContentInt(par1ItemStack, "level");
		  for (int i = 0; i != canHarvestBlock.size(); i++) {
			  if ((par2Block == canHarvestBlock) &&
					  (level <= digStrength.length)) {
				  return digStrength[(level - 1)];
			  }
		  }
		  if (((par2Block.getMaterial() == Material.rock) || (par2Block.getMaterial()) == Material.sand) || (par2Block.getMaterial() == Material.cactus) &&
				  (level <= digStrength.length)) {
			  return digStrength[(level - 1)];
		  }
		  return 1.0F;
	  }

	  @Override
	  public float getDigSpeed(ItemStack stack, Block block, int meta){
		  ensureTagCompound(stack);
		  int level = getSeitiTagCompoundContentInt(stack, "level");
		  for (int i = 0; i != canHarvestBlock.size(); i++) {
			  if ((block == canHarvestBlock) &&
					  (level <= digStrength.length)) {
				  return digStrength[(level - 1)];
			  }
		  }
		  if (((block.getMaterial() == Material.rock) || (block.getMaterial()) == Material.sand) || (block.getMaterial() == Material.cactus) &&
				  (level <= digStrength.length)) {
			  return digStrength[(level - 1)];
		  }
		  return 1.0F;
	  }

	  @Override
		public float func_150893_a(ItemStack itemStack, Block block) {
			if (block.getMaterial() == Material.rock) {
				return this.efficiencyOnProperMaterial;
			}
			return super.func_150893_a(itemStack, block);
		}


	class PickaxeDummy
	  {
	    private ToolMaterial toolMaterial;

	    public PickaxeDummy()
	    {
	      this.toolMaterial = ToolMaterial.WOOD;
	    }

	    public boolean checkHarvest(Block par1Block, ToolMaterial par2Material)
	    {
	      this.toolMaterial = par2Material;
	      return this.toolMaterial.getHarvestLevel() == 3;
	    }
	  }

	  public boolean canHarvestBlock(Block par1Block, ItemStack itemStack)
	  {
	    ensureTagCompound(itemStack);
	    int level = getSeitiTagCompoundContentInt(itemStack, "level");
	    ToolMaterial par2Material;
	    if (level < 4)
	    {
	      par2Material = ToolMaterial.WOOD;
	    }
	    else
	    {
	      if (level < 7)
	      {
	        par2Material = ToolMaterial.STONE;
	      }
	      else
	      {
	        if (level < 9) {
	          par2Material = ToolMaterial.IRON;
	        } else {
	          par2Material = ToolMaterial.EMERALD;
	        }
	      }
	    }
	    boolean result = this.dummy.checkHarvest(par1Block, par2Material);
	    if (!result) {
	      result = par1Block == null;
	    }
	    return result;
	  }

	  public boolean isDamageable()
	  {
	    return false;
	  }

	  public static ItemStack ensureTagCompound(ItemStack stack)
	  {
	    if (!stack.hasTagCompound()) {
	      stack.setTagCompound(new NBTTagCompound());
	    }
	    if (!checkHasSeitiTag(stack)) {
	      stack.getTagCompound().setTag("seitiattribute", new NBTTagCompound());
	    }
	    int oldexp = 0;
	    int oldlevel = 1;
	    if (stack.getTagCompound().hasKey("level")) {
	      oldlevel = stack.getTagCompound().getInteger("level");
	    }
	    if (stack.getTagCompound().hasKey("exp")) {
	      oldexp = stack.getTagCompound().getInteger("exp");
	    }
	    if (!checkHasTaginSeitiTag(stack, "level")) {
	      setSeitiTagCompoundContentInt(stack, "level", Integer.valueOf(oldlevel), Integer.valueOf(0));
	    }
	    if (!checkHasTaginSeitiTag(stack, "exp")) {
	      setSeitiTagCompoundContentInt(stack, "exp", Integer.valueOf(oldexp), Integer.valueOf(0));
	    }
	    if (!checkHasTaginSeitiTag(stack, "expd")) {
	      setSeitiTagCompoundContentInt(stack, "expd", Integer.valueOf(oldexp), Integer.valueOf(0));
	    }
	    return stack;
	  }

	  public static boolean checkHasSeitiTag(ItemStack stack)
	  {
	    if (stack.hasTagCompound()) {
	      return stack.getTagCompound().hasKey("seitiattribute");
	    }
	    return false;
	  }

	  public static boolean checkHasTaginSeitiTag(ItemStack stack, String name)
	  {
	    if (checkHasSeitiTag(stack))
	    {
	      NBTTagCompound tag = getSeitiTagCompound(stack);
	      return tag.hasKey(name);
	    }
	    return false;
	  }

	  public static int getSeitiTagCompoundContentInt(ItemStack stack, String name)
	  {
	    if (checkHasTaginSeitiTag(stack, name)) {
	      return getSeitiTagCompound(stack).getInteger(name);
	    }
	    return 0;
	  }

	  public static NBTTagCompound getSeitiTagCompound(ItemStack stack)
	  {
	    if (checkHasSeitiTag(stack)) {
	      return (NBTTagCompound)stack.getTagCompound().getTag("seitiattribute");
	    }
	    return null;
	  }

	  public static void setSeitiTagCompoundContentInt(ItemStack stack, String name, Integer i, Integer shift)
	  {
	    if (checkHasSeitiTag(stack)) {
	      getSeitiTagCompound(stack).setInteger(name, i.intValue() + shift.intValue());
	    }
	  }

	  public static void addSeitiTagCompoundContentInt(ItemStack stack, String name, Integer shift)
	  {
	    if (checkHasSeitiTag(stack))
	    {
	      int i = getSeitiTagCompound(stack).getInteger(name);
	      setSeitiTagCompoundContentInt(stack, name, Integer.valueOf(i), shift);
	    }
	  }

	  public ItemStack getContainerItemStack(ItemStack stack)
	  {
	    ItemStack copied = stack.copy();
	    if (copied.hasTagCompound())
	    {
	      int expd = getSeitiTagCompoundContentInt(stack, "expd");
	      int exp = getSeitiTagCompoundContentInt(stack, "exp");
	      setSeitiTagCompoundContentInt(copied, "exp", Integer.valueOf(exp), Integer.valueOf(-expd));
	    }
	    return copied;
	  }

	  public boolean hasContainerItem()
	  {
	    return true;
	  }



	/*public void onCreated(ItemStack stack, World world, EntityPlayer player){
		NBTTagCompound nbt = stack.getTagCompound();

		addExp(500000);
		if(nbt != null){
			nbt = new NBTTagCompound();

			nbt.setInteger("Exp", this.exp);
			nbt.setInteger("Max Exp", this.maxExp);
			nbt.setInteger("Level", this.level);
			nbt.setInteger("Max Level", this.maxLevel);

			stack.setTagCompound(nbt);
		}
		System.out.println("onCreated");
	}

	@SuppressWarnings("unchecked")
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
	{
		//list.add(EnumChatFormatting.LIGHT_PURPLE + "EnergyStored: " + this.getEnergyStored(stack) + " / " + this.maxEnergy);
		//list.add(EnumChatFormatting.GRAY + "-----------------");

		list.add(EnumChatFormatting.GOLD + "Current EXP: " + EnumChatFormatting.GREEN + stack.stackTagCompound.getInteger("Exp") +
				EnumChatFormatting.GRAY + " / " + EnumChatFormatting.DARK_RED + stack.stackTagCompound.getInteger("Max Exp"));

		list.add(EnumChatFormatting.GOLD + "Current LEVEL: " + EnumChatFormatting.GREEN + stack.stackTagCompound.getInteger("Level") +
				EnumChatFormatting.GRAY + " / "+ EnumChatFormatting.DARK_RED + stack.stackTagCompound.getInteger("Max Level"));
		list.add(EnumChatFormatting.GOLD + "Current EXP: " + EnumChatFormatting.GREEN + this.getExp() +
				EnumChatFormatting.GRAY + " / " + EnumChatFormatting.DARK_RED + this.getMaxExp());
		list.add(EnumChatFormatting.GOLD + "Current LEVEL: " + EnumChatFormatting.GREEN + this.getLevel() +
				EnumChatFormatting.GRAY + " / "+ EnumChatFormatting.DARK_RED + this.getMaxLevel());
		if(GregLife.proxy.isShiftKeyDown()){
			list.add("-----------------------");
			//list.add("Max Storage: " + blockFurnace.getEnergyCapacity());
			//list.add("Max Transfer: " + blockFurnace.getEnergyTransfer());
		}else{
			list.add(EnumChatFormatting.DARK_PURPLE + "§o--Press Shift for Info--");
		}
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5){
		while(exp >= maxExp || exp < 0){
			if(canLevelUp()){
				levelUp();
			}else if(canLevelDown()){
				levelDown();
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity){
		//if(this.canHarvestBlock == world.getBlock(x, y, z)){
			this.exp += 10;

			NBTTagCompound nbt = stack.getTagCompound();

			//extractEnergy(stack, nbt.getInteger("Level") * energyPerDamage, false);

			//一度だけ実行
			for(int i = 0; i < 1; i++){
				exp = nbt.getInteger("Exp");
				maxExp = stack.getTagCompound().getInteger("Max Exp");
				level = stack.getTagCompound().getInteger("Level");
				maxLevel = stack.getTagCompound().getInteger("Max Level");
				Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("Loaded NBT's"));
				i = 0;
			}

			addExp(500000);
			if(nbt != null){
				nbt = new NBTTagCompound();

				//nbt.setInteger("Energy", this.getEnergyStored(stack));
				nbt.setInteger("Exp", this.exp);
				nbt.setInteger("Max Exp", this.maxExp);
				nbt.setInteger("Level", this.level);
				nbt.setInteger("Max Level", this.maxLevel);

				stack.setTagCompound(nbt);
			}

			return true;
		//}
		//return false;
	}

	public int getExp(){
		return exp;
	}

	public int getMaxExp(){
		return maxExp;
	}

	public int getLevel(){
		return level;
	}

	public int getMaxLevel(){
		return maxLevel;
	}

	public ItemCosmosPick setExp(int exp){
		this.exp = exp;
		return this;
	}

	public ItemCosmosPick setMaxExp(int maxExp){
		this.maxExp = maxExp;
		return this;
	}

	public ItemCosmosPick setLevel(int level){
		this.level = level;
		return this;
	}

	public ItemCosmosPick setMaxLevel(int maxLevel){
		this.maxLevel = maxLevel;
		return this;
	}


	public ItemCosmosPick addExp(int exp){
		this.exp += exp;
		return this;
	}

	public ItemCosmosPick removeExp(int exp){
		this.exp -= exp;
		return this;
	}

	public ItemCosmosPick addExp(int exp, int modifier){
		this.exp += (exp * modifier);
		return this;
	}

	public ItemCosmosPick removeExp(int exp, int modifier){
		this.exp -= (exp * modifier);
		return this;
	}

	public boolean isLeveledUp(){
		if (level == maxLevel){
			return true;
		}else{
			return false;
		}
	}

	public boolean isMaxed(){
		if (exp == maxExp && isLeveledUp()){
			return true;
		}else{
			return false;
		}
	}

	public boolean canLevelUp(){
		if (exp >= maxExp && !isLeveledUp()){
			return true;
		}else{
			return false;
		}
	}

	public boolean canLevelDown(){
		if (exp < 0){
			if (level != 1){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}

	public boolean canEarnExp(){
		if (exp > 0 || exp <= maxExp){
			return true;
		}else{
			return false;
		}
	}

	public boolean canLoseExp(){
		if (exp > 0){
			return true;
		}else{
			return false;
		}
	}

	public void levelUp(){
		level++;
		exp = (exp - maxExp);
		maxExp = (maxExp + (15 * level));

		Minecraft mc = Minecraft.getMinecraft();
		mc.thePlayer.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + "Level Up to: " + this.getLevel()));
		mc.thePlayer.playSound("random.levelup", 1.0F, 1.0F);
	}

	public void levelDown(){
		level--;
		exp = (maxExp - (exp * -1));
		maxExp = (maxExp + (15 * level));
	}*/
}
