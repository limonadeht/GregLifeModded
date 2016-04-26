package greglife.tileentity;

import greglife.entity.EntityTime;
import net.minecraft.nbt.NBTTagCompound;

public class TileTimeTorch extends TileEntityBase{

	public NBTTagCompound upgrades;
	//public byte color;
	public int timeType;
	private int timeout;
	private int maxTimeout;

	public TileTimeTorch(){
		this.upgrades = new NBTTagCompound();
		//this.color = 0;
		this.timeType = 1;
	}

	public boolean canUpdate(){
		return true;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void updateEntity(){
		{
			if(/*(!Proxies.common.isSimulating(this.field_70331_k)) ||*/ (this.upgrades == null) || (this.upgrades.hasNoTags())){
				return;
			}
			this.timeout += 1;
			if(this.timeout < this.maxTimeout){
				return;
			}
			this.timeout = 0;
			this.maxTimeout = (150 + (int)(this.worldObj.rand.nextFloat() * 150 / 2.0F));
			int light;
			if(this.upgrades.hasKey("Light")){
				light = this.upgrades.getInteger("Light");
			}
			int healing;
			if(this.upgrades.hasKey("Healing")){
				/*boolean healed;
				try{
					AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.xCoord - 4, this.yCoord - 4, this.zCoord - 4, this.xCoord + 4, this.yCoord + 4, this.zCoord + 4);

					List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);

					healed = false;

					healing = 1;
					for(EntityLivingBase entity : entities){
						if(entity != null){
							if((entity.isEntityUndead()) || ((entity instanceof EntityGhast))){
								entity.attackEntityFrom(PunishDamage.instance, 2.0F);
							}else{
								float maxHealth = (float)entity.getAttributeMap().func_111152_a("generic.maxHealth").func_111126_e();
								if(entity.getHealth() < maxHealth){
									entity.heal(healing * 2);
									healed = true;

									PacketHelper.sendCureFXToClients(entity, 8 * healing);
								}
							}
							if(healed){
								this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "dartcraft:cure", 0.5F, Utils.randomPitch());
							}
						}
					}
				}
				catch (Exception e) {}*/
			}
			Object entity;
			if(this.upgrades.hasKey("Bane")){
				try
				{
					/*AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.xCoord - 4, this.yCoord - 4, this.zCoord - 4, this.xCoord + 4, this.yCoord + 4, this.zCoord + 4);

					List<EntityLivingBase> entities = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, bb);
					boolean removed = false;
					for(healing = entities.iterator(); healing.hasNext();){
						entity = (EntityLivingBase)healing.next();
						if(entity != null){
							if((((entity instanceof EntityMob)) || ((entity instanceof EntitySlime)) || ((entity instanceof EntityGhast))) && (!(entity instanceof EntityWitch)) && (!(entity instanceof EntityWither))){
								if(!BottlingBlacklist.isEntityBlacklisted(entity.getClass())){
									this.worldObj.removeEntity((Entity)entity);
									removed = true;

									PacketHelper.sendChangeFXToClients((Entity)entity, 16);
								}
							}
						}
					}
					if(removed){
						this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "random.pop", 1.0F, Utils.randomPitch());
					}*/
				}
				catch (Exception e) {}
			}
			if(this.upgrades.hasKey("Heat")){
				/*try{
					AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(this.field_70329_l - Config.torchDist, this.field_70330_m - Config.torchDist, this.field_70327_n - Config.torchDist, this.field_70329_l + Config.torchDist, this.field_70330_m + Config.torchDist, this.field_70327_n + Config.torchDist);

					List<EntityLivingBase> entities = this.field_70331_k.getEntitiesWithinAABB(EntityLivingBase.class, bb);
					boolean burned = false;

					int heat = 1;
					for(entity = entities.iterator(); ((Iterator)entity).hasNext();){
						EntityLivingBase entity = (EntityLivingBase)((Iterator)entity).next();
						if((entity != null) && (!entity.isImmuneToFire())){
							NBTTagCompound entityUpgrades = new NBTTagCompound();
							if((entity instanceof EntityPlayer)){
								EntityPlayer player = (EntityPlayer)entity;
								entityUpgrades = SocketHelper.getArmorCompound(player);
							}
							if((!entityUpgrades.hasKey("Heat")) || (entityUpgrades.getInteger("Heat") < 3)){
								entity.setFire(heat);
								entity.attackEntityFrom(DamageSource.inFire, 0.5F * heat);

								burned = true;

								PacketHelper.sendHeatFXToClients(entity, 8 * heat, 0);
							}
						}
					}
					if (burned) {
						this.worldObj.playSoundEffect(this.xCoord, this.yCoord, this.zCoord, "dartcraft:ignite", 1.0F, Utils.randomPitch());
					}
				}
				catch (Exception e) {}*/
			}
			/*if((this.upgrades.hasKey("Repair")) && (Loader.isModLoaded("Thaumcraft"))){
				label971:
					try
			{
						for (int i = -Config.torchDist; i < Config.torchDist; i++) {
							for (int j = -Config.torchDist; j < Config.torchDist; j++) {
								for (int k = -Config.torchDist; k < Config.torchDist; k++)
								{
									TileEntity tile = this.field_70331_k.func_72796_p(this.field_70329_l + i, this.field_70330_m + j, this.field_70327_n + k);
									if ((tile != null) && (ThaumCraftIntegration.isDeconstructor(tile)))
									{
										ThaumCraftIntegration.setDeconAspect(tile);
										break label971;
									}
								}
							}
						}
			}
			catch (Exception e) {}
			}*/
			if ((this.upgrades.hasKey("Time")) /*&& (Config.timeUpgradeTorch)*/)
			{
				EntityTime time = new EntityTime(this.worldObj, this.maxTimeout - 2);
				time.posX = (this.xCoord + 0.5D);
				time.posY = (this.yCoord + 0.5D);
				time.posZ = (this.zCoord + 0.5D);
				time.setType(this.timeType, false);

				this.worldObj.spawnEntityInWorld(time);
			}
		}
	}

	@Override
	public void readCustomNBT(NBTTagCompound comp, boolean descPacket){
		this.upgrades = comp.getCompoundTag("upgrades");
	    //this.color = comp.getByte("color");

	    this.timeType = comp.getByte("timeType");
	}

	@Override
	public void writeCustomNBT(NBTTagCompound comp, boolean descPacket){
	    comp.setTag("upgrades", this.upgrades);
	    //comp.setByte("color", this.color);
	    comp.setByte("timeType", (byte)this.timeType);
	}
}
