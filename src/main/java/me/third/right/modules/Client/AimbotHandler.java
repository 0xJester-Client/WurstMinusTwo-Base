package me.third.right.modules.Client;

import me.third.right.ThirdMod;
import me.third.right.events.event.RenderEvent;
import me.third.right.modules.HackClient;
import me.third.right.settings.setting.CheckboxSetting;
import me.third.right.settings.setting.EnumSetting;
import me.third.right.settings.setting.SliderSetting;
import me.third.right.utils.Client.Enums.RotationsType;
import me.third.right.utils.Client.Manage.RotationHandler;
import me.third.right.utils.Client.Utils.ChatUtils;
import me.third.right.utils.Render.ThreeDRender;
import me.third.right.utils.Wrapper;
import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import static me.third.right.utils.Client.Utils.Colour.rgbToInt;
import static me.third.right.utils.Client.Utils.EntityUtils.getPositionEyesVec;
import static me.third.right.utils.Client.Utils.RotationUtils.getVectorForRotation;
import static me.third.right.utils.Client.Utils.RotationUtils.rayTrace;

public class AimbotHandler extends HackClient {
    //Vars
    private final ICamera camera = new Frustum();
    //Settings
    public final EnumSetting<RotationsType> rotationType = setting(new EnumSetting<>("RotationType", "", RotationsType.values(), RotationsType.Raytrace));
    public final CheckboxSetting randomRotations = setting(new CheckboxSetting("RandomRotations", "", true));
    public final SliderSetting resetDelaySeconds = setting(new SliderSetting("ResetDelaySeconds", "", 2, 0, 5, 1, SliderSetting.ValueDisplay.INTEGER));
    public final CheckboxSetting noSpoof = setting(new CheckboxSetting("NoSpoof", "", false));
    public final CheckboxSetting drawSpoofed = setting(new CheckboxSetting("DrawSpoofed", "", false));

    public AimbotHandler() {
        super("AimbotHandler", "Handles rotations for modules.");
        ThirdMod.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    private final Listener<RenderEvent> renderEventListener = new Listener<>(event -> {
        if(!drawSpoofed.isChecked() || mc.getRenderViewEntity() == null) return;
        if(RotationHandler.rotation != null) {
            final RayTraceResult traceResult = rayTrace(6, RotationHandler.rotation.getFirst(), RotationHandler.rotation.getSecond());
            if(traceResult != null && traceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
                camera.setPosition(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().posY, mc.getRenderViewEntity().posZ);
                final IBlockState iBlockState = mc.world.getBlockState(traceResult.getBlockPos());
                final AxisAlignedBB axisAlignedBB = iBlockState.getSelectedBoundingBox(mc.world, traceResult.getBlockPos());
                if(camera.isBoundingBoxInFrustum(axisAlignedBB)) {
                    ThreeDRender.prepare();
                    ThreeDRender.drawBoundingBox(axisAlignedBB, 2F, rgbToInt(255, 0, 0, 150));
                    ThreeDRender.release();
                }
            }
        }
    });

    @Override
    public void onDisable() {
        this.setEnabled(true);
    }

    public static RayTraceResult rayTrace(final double blockReachDistance, final float pitch, final float yaw) {
        final Vec3d vec3d = getPositionEyesVec(mc.player);
        final Vec3d vec3d1 = getVectorForRotation(pitch,yaw);
        final Vec3d vec3d2 = vec3d.add(vec3d1.x * blockReachDistance, vec3d1.y * blockReachDistance, vec3d1.z * blockReachDistance);
        return mc.world.rayTraceBlocks(vec3d, vec3d2, false, true, true);
    }
}
