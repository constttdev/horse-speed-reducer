package work.constt.horse_speed_reducer.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.text.Text;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class Horse_speed_reducerClient implements ClientModInitializer {

    private static KeyBinding reduceHorseSpeedKey;
    public static final Identifier SPEED_REDUCE_PACKET_ID = new Identifier("my_mario", "reduce_horse_speed");

    @Override
    public void onInitializeClient() {
        KeyBinding reduceHorseSpeedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.horse_speed_reducer.reduce_horse_speed",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                "category.horse_speed_reducer.keys"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (reduceHorseSpeedKey.wasPressed()) {
                if (client.player != null && client.player.getVehicle() instanceof net.minecraft.entity.passive.HorseEntity) {
                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    ClientPlayNetworking.send(SPEED_REDUCE_PACKET_ID, buf);
                }
            }
        });
    }
}
