package greglife.entity;

import java.util.ArrayList;
import java.util.List;

import greglife.tileentity.TileTimeTorch;
import greglife.util.FXutil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityTime
  extends Entity
{
  public static final int TYPE_NONE = 0;
  public static final int TYPE_STOP = 1;
  public static final int TYPE_SLOW = 2;
  public static final int TYPE_FAST = 3;
  public static final int TYPE_HYPER = 4;
  public int type;
  public static double RANGE = 6.0D;
  private int lifeTime;
  private boolean checked;

  private static ArrayList<Class> tileBlacklist = new ArrayList();
  private static ArrayList<Block> dismantleables = new ArrayList();
  private static ArrayList<String> whitelist = new ArrayList();

  public EntityTime(World world)
  {
	  super(world);

	  this.lifeTime = 200;
	  this.checked = false;
  }

  public EntityTime(World world, int life)
  {
	  this(world);

	  this.lifeTime = life;
  }

  protected void entityInit() {}

  public void onUpdate()
  {
	  this.lifeTime -= 1;
	  if(this.lifeTime <= 0) {
		  destroy();
	  }else{
		  customTick();
	  }
  }

  private void customTick()
  {
	  if((/*(Proxies.common.isSimulating(this.worldObj)) &&*/ (this.lifeTime % 20 == 0)) || (!this.checked)){
		  spawnParticles();
	  }
	  if((this.lifeTime % 5 == 0) || (!this.checked)){
		  checkEntities();
	  }
  }

  @SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
  public void checkEntities()
  {
	  boolean timeAffectBlocks = true;
	  boolean timeAffectTiles = true;
	  boolean timeUpgradeRod = true;
	  boolean timeUpgradeSword = true;
	  boolean timeUpgradeTorch = true;

	  boolean timeAffectWhitelist = false;
	  try{
		  if((!timeAffectBlocks) && (!timeAffectTiles) && (!timeUpgradeRod) && (!timeUpgradeSword) && (!timeUpgradeTorch)){
			  return;
		  }
		  AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.posX - RANGE, this.posY - RANGE, this.posZ - RANGE, this.posX + RANGE, this.posY + RANGE, this.posZ + RANGE);

		  ArrayList<Entity> entities = new ArrayList();
		  List<Entity> found = this.worldObj.getEntitiesWithinAABB(Entity.class, bb);
		  for (Entity entity : found) {
			  if((entity != null) && (!(entity instanceof EntityTime)) && (!(entity instanceof EntityPlayer)) /*&& (!(entity instanceof EntityBottle)) && (!(entity instanceof EntityInvincibleItem))*/) {
				  entities.add(entity);
			  }
		  }
		  for(Entity entity : entities){
			  NBTTagCompound dartTag = getDartData(entity);
			  if(/*(!(entity instanceof EntityFrozenItem)) &&*/ (!(entity instanceof EntityItem))){
				  if (dartTag.getInteger("timeImmune") <= 0){
					  dartTag.setInteger("time", this.type);
					  dartTag.setInteger("timeTime", this.lifeTime);
				  }
			  }else{
				  switch(this.type){
				  case 0:
				  default:
					  break;
				  case 2:
					  break;
				  case 1:
					  if((entity instanceof EntityFrozenItem)){
              //DebugUtils.print("Frozen item found.");
						  System.out.println("Frozen item not found.");
            }
					  if (((entity instanceof EntityArrow)) && ((entity.posX != entity.prevPosX) || (entity.posY != entity.prevPosY) || (entity.posZ != entity.prevPosZ)))
            {
              EntityFrozenItem frozen = new EntityFrozenItem(this.worldObj, entity, this.lifeTime);

              this.worldObj.removeEntity(entity);
              this.worldObj.spawnEntityInWorld(frozen);
            }
					  if((entity instanceof EntityItem)){
						  EntityItem item = (EntityItem)entity;
						  if(dartTag.hasKey("timeImmune")){
							  dartTag.setInteger("timeImmune", dartTag.getInteger("timeImmune") - 1);
							  if(dartTag.getInteger("timeImmune") <= 0){
								  dartTag.removeTag("timeImmune");
							  }
						  }else if((item.getEntityItem() != null) && (item.getEntityItem().stackSize >= 1)){
							  int itemLifeTime = 600;
							  EntityFrozenItem frozen = new EntityFrozenItem(this.worldObj, item, itemLifeTime);

							  item.setEntityItemStack(item.getEntityItem().splitStack(0));
							  this.worldObj.removeEntity(item);
							  this.worldObj.spawnEntityInWorld(frozen);
						  }
					  }
					  break;
				  case 3:
				  }
			  }
		  }
		  if((this.type == 3) || (this.type == 4)){
			  for(int i = (int)(this.posX - RANGE); i < (int)(this.posX + RANGE); i++){
				  for(int j = (int)(this.posY - RANGE); j < (int)(this.posY + RANGE); j++){
					  for(int k = (int)(this.posZ - RANGE); k < (int)(this.posZ + RANGE); k++){
						  float chance = this.type == 4 ? 0.5F : 0.1F;
						  try{
							  if (timeAffectBlocks)
							  {
								  int id = this.worldObj.getBlock(i, j, k).getIdFromBlock(null);
								  //if((id > 0) && (id < this.worldObj.getBlock(i, j, k).getBlockById(0))){
									  Block block = this.worldObj.getBlock(i, j, k);
									  if ((block != null) &&
											  (chance >= this.rand.nextFloat())) {
										  block.updateTick(this.worldObj, i, j, k, this.rand);
									  }
								  //}
							  }
						  }catch(Exception e){
							  //DebugUtils.printError(e);
							  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
						  }
						  int freq = this.type == 4 ? 20 : 5;
						  try{
							  TileEntity tile = this.worldObj.getTileEntity(i, j, k);
							  if((tile != null) && (!(tile instanceof TileTimeTorch))){
								  if ((timeAffectTiles) && ((!timeAffectWhitelist) ||
										  (isTileBlacklisted(tile.getClass()))))
								  {
									  if((tile instanceof TileEntityMobSpawner)){
										  TileEntityMobSpawner spawner = (TileEntityMobSpawner)tile;
										  if(!(spawner.func_145881_a() instanceof CustomEntityLogic)){
											  String name = spawner.func_145881_a().getEntityNameToSpawn();

											  CustomEntityLogic logic = new CustomEntityLogic(spawner, this.type);
											  SpawnerReflector.setLogic(spawner, logic);
										  }else{
											  CustomEntityLogic logic = (CustomEntityLogic)spawner.func_145881_a();
											  logic.setHyper();
										  }
									  }
									  boolean handleTile = false;
									  if(!/*ForestryTimer.*/handleTile/*(tile, freq)*/){
										  for (int l = 0; l < freq; l++) {
											  tile.markDirty();
										  }
									  }
								  }
							  }
						  }catch (Exception e){
							  //DebugUtils.printError(e);
							  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
						  }
					  }
				  }
			  }
		  }
	  }catch(Exception e){
		  //DebugUtils.printError(e);
		  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
	  }
	  this.checked = true;
  }

	  public static boolean isTileBlacklisted(Class tileClass)
	  {
		  boolean whitelistWrench = true;
		  try
		  {
			  if ((tileClass != null) && (tileClass.getCanonicalName() != null))
			  {
				  String name = tileClass.getCanonicalName();
				  if (whitelistWrench)
				  {
					  for (String check : whitelist) {
						  if (check.equals(name)) {
							  return false;
						  }
					  }
					  return true;
				  }
				  if ((name.contains("enhancedportals")) || (name.contains("RotaryCraft")) ||
						  (name.contains("gregtechmod"))) {
					  return true;
				  }
				  if ((tileBlacklist != null) && (tileBlacklist.size() > 0)) {
					  for (Class check : tileBlacklist) {
						  if (check.equals(tileClass)) {
							  return true;
						  }
					  }
				  }
			  }
		  }
		  catch (Exception e)
		  {
			  //DebugUtils.printError(e);
			  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
		  }
		  return false;
	  }

  public static NBTTagCompound getDartData(Entity entity)
  {
	  NBTTagCompound comp = new NBTTagCompound();
	  if ((entity != null) && (entity.getEntityData() != null))
	  {
		  if (!entity.getEntityData().hasKey("DartCraft")) {
			  entity.getEntityData().setTag("DartCraft", new NBTTagCompound());
		  }
		  comp = entity.getEntityData().getCompoundTag("DartCraft");
	  }
	  return comp;
  }

  public void destroy(){
	  setDead();
  }

  public void spawnParticles(){
	  boolean timeParticles = true;
	  if((!timeParticles) || (this.lifeTime < 20)){
		  return;
	  }
	  int type = 4;
	  switch (this.type)
	  {
	  case 0:
		  return;
	  case 1:
	  case 2:
		  type = 3;
		  break;
	  case 3:
		  type = 2;
		  break;
	  case 4:
		  type = 1;
	  }
	  sendTimeFXToClients(this.worldObj, this.posX, this.posY, this.posZ, type, 1, 0);
	  FXutil.makeTimeEffects(worldObj, posX, posY, posZ,  1, 1, 1);
  }

  public static void sendTimeFXToClients(World world, double x, double y, double z, int type, int amount, int area){
	  //if(Proxies.common.isSimulating(world)){
	  NBTTagCompound fxComp = new NBTTagCompound();
	  fxComp.setInteger("type", 8);

	  fxComp.setInteger("area", area);
	  fxComp.setInteger("amount", amount);
	  fxComp.setDouble("x", x);
	  fxComp.setDouble("y", y);
	  fxComp.setDouble("z", z);
	  fxComp.setInteger("subType", type);

	  //PacketBuffer.sendPacketToAllAround(x, y, z, 80.0D, world.provider.dimensionId, new PacketNBT(54, fxComp).getPacket());
	  //}
  }

  public boolean setType(int type, boolean playSound){
	  if((type >= 0) && (type <= 4)){
		  this.type = type;
		  if(playSound){
			  String sound = "dartcraft:";
			  switch (this.type){
			  case 1:
			  case 2:
				  sound = sound + "slowDown";
				  break;
			  case 3:
			  case 4:
				  sound = sound + "speedUp";
			  }
			  if(this.worldObj != null){
				  this.worldObj.playSoundAtEntity(this, sound, 1.0F, 1.0F);
			  }
		  }
		  return true;
	  }
	  this.type = 0;
	  return false;
  }

  public void setLife(int life){
	  this.lifeTime = life;
  }

  public int getType(){
	  return this.type;
  }

  protected void readEntityFromNBT(NBTTagCompound comp){
	  try{
		  setType(comp.getInteger("timeType"), false);

		  this.lifeTime = comp.getInteger("lifeTime");
	  }catch(Exception e){
		  //DebugUtils.printError(e);
		  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
	  }
  }

  protected void writeEntityToNBT(NBTTagCompound comp){
	  try{
		  comp.setInteger("timeType", this.type);
		  comp.setInteger("lifeTime", this.lifeTime);
	  }catch(Exception e){
		  //DebugUtils.printError(e);
		  System.out.println("[GregLifeMod/Err] Time Torch Error. Please Report to mod author");
	  }
  }
}
