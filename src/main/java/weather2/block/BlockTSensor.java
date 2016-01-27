package weather2.block;

import java.util.Random;

import CoroUtil.util.Vec3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import weather2.ServerTickHandler;
import weather2.config.ConfigMisc;
import weather2.weathersystem.WeatherManagerServer;
import weather2.weathersystem.storm.StormObject;

public class BlockTSensor extends Block
{
	
	public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
	
    public BlockTSensor(int var1)
    {
        super(Material.clay);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
        this.setTickRandomly(true);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /*@Override
    public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
        this.updateTick(world, pos, state, rand);
    }*/

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand)
    {
    	
    	if (world.isRemote) return;
    	
        //var1.getBlockStateMetadata(var2, var3, var4);
        //List var7 = var1.getEntitiesWithinAABB(EntTornado.class, AxisAlignedBB.getBoundingBoxFromPool((double)var2, (double)var3, (double)var4, (double)var2 + 1.0D, (double)var3 + 1.0D, (double)var4 + 1.0D).expand(140.0D, 140.0D, 140.0D));
    	
    	boolean enable = false;
    	
    	WeatherManagerServer wms = ServerTickHandler.lookupDimToWeatherMan.get(world.provider.getDimensionId());
    	if (wms != null) {
    		StormObject so = wms.getClosestStorm(new Vec3(pos.getX(), pos.getY(), pos.getZ()), ConfigMisc.sensorActivateDistance, StormObject.STATE_FORMING);
    		if (so != null/* && so.attrib_tornado_severity > 0*/) {
    			enable = true;
    		}
    	}

        if (enable)
        {
        	world.setBlockState(pos, state.withProperty(POWER, 15), 3);
        }
        else
        {
        	world.setBlockState(pos, state.withProperty(POWER, 0), 3);
        }

        /*if(var7.size() > 0) {
           var1.setBlockStateMetadataWithNotify(var2, var3, var4, 15);
        } else {
           var1.setBlockStateMetadataWithNotify(var2, var3, var4, 0);
        }*/
        /*world.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this);
        world.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this);
        world.notifyBlocksOfNeighborChange(var2, var3, var4, this);
        world.markBlockRangeForRenderUpdate(var2, var3, var4, var2, var3, var4);
        world.scheduleBlockUpdate(var2, var3, var4, this, this.tickRate(var1));*/
    }

    /*@Override
    public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return var1.getBlockStateMetadata(var2, var3, var4) == 0 ? 0 : 15;
    }

    @Override
    public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5)
    {
        return var1.getBlockStateMetadata(var2, var3, var4) == 0 ? 0 : 15;
    }*/

    public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }
    
    @Override
    public boolean canProvidePower()
    {
        return true;
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWER, Integer.valueOf(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state)
    {
        return ((Integer)state.getValue(POWER)).intValue();
    }

    protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {POWER});
    }
}
