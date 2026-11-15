package melonystudios.reutilities.client.api;

import melonystudios.reutilities.api.ReAPI;
import melonystudios.reutilities.component.ReDataComponents;
import melonystudios.reutilities.option.ReCommonOptions;
import melonystudios.reutilities.tag.ReItemTags;
import melonystudios.reutilities.util.debug.ReDebuggingFlags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import static melonystudios.reutilities.client.ReClientConstants.*;

/// ***Reutilities'*** **client API** class.
public class ReClientAPI {
    /// Adds an `/mstudios` command identifier to the [list of `/mstudios help` commands][melonystudios.reutilities.client.ReClientConstants#STUDIOS_COMMAND_IDENTIFIERS].
    ///
    /// This method should be called during the [client setup event][net.neoforged.fml.event.lifecycle.FMLClientSetupEvent].
    /// @param modID The ID of the mod adding the command.
    /// @param identifier The identifier of the command, such as `develop/dump_boat_types` or `visual/set_resolution`. **There must always be a `/` separating the type and the name!**
    public static void registerStudiosCommand(String modID, String identifier) {
        STUDIOS_COMMAND_IDENTIFIERS.put(identifier, modID);
    }

    /// Gets the translated text for a translation key, and uses a fallback if not available.
    /// @param key The translation key to use and check.
    /// @param fallback A fallback string to use, using `%s` for arguments.
    /// @param args An optional array of arguments.
    public static String translate(String key, String fallback, Object... args) {
        return I18n.exists(key) ? I18n.get(key, args) : String.format(fallback, args);
    }

    /// Gets the brightness that should be applied to an item, based on its presence in the [`#c:emissive_lighting`][ReItemTags#EMISSIVE_LIGHTING] item tag,
    /// its [`reutilities:light_emission`][ReDataComponents#LIGHT_EMISSION] component, and the block item's brightness.
    ///
    /// [Skylight][#getSkylight(Level, int)] is taken into consideration only when the world and position aren't `null`.
    /// @param stack The item stack to make emissive.
    /// @param lightEmission The old light value of this item, usually the `packedLight` parameter.
    /// @param level *(optional)* The world.
    /// @param pos *(optional)* The location in the world this item is in.
    /// @param applySkylight Whether skylight should be considered when calculating the light.
    public static int getItemBrightness(ItemStack stack, int lightEmission, Level level, BlockPos pos, boolean applySkylight) {
        float skylight = applySkylight ? getSkylight(level, lightEmission) : 15;
        int emittedBlockLight = ReAPI.getEmittedBlockLight(stack, level, pos);
        int ambientBlockLight = LightTexture.block(lightEmission);

        if (ReDebuggingFlags.DEBUG_LIGHT_EMISSION_DISPLAY && level.isClientSide()) {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty() && ItemStack.isSameItemSameComponents(stack, player.getItemBySlot(EquipmentSlot.MAINHAND))) {
                player.displayClientMessage(Component.translatable(
                        "debug.reutilities.light_emission_display",
                        emittedBlockLight,
                        skylight,
                        ambientBlockLight), true);
            }
        }

        var emissionComponent = stack.get(ReDataComponents.LIGHT_EMISSION);
        if (emissionComponent != null) {
            int maxLight = (int) Math.max(ambientBlockLight, Math.max(emissionComponent, skylight));
            return LightTexture.pack(maxLight, maxLight);
        } else if (stack.is(ReItemTags.EMISSIVE_LIGHTING)) {
            return EMISSIVE_LIGHT_VALUE;
        } else if (ReCommonOptions.LIGHT_EMITTING_EMISSIVES.get() && emittedBlockLight > 0) {
            int maxLight = (int) Math.max(ambientBlockLight, Math.max(emittedBlockLight, skylight));
            return LightTexture.pack(maxLight, maxLight);
        }
        return lightEmission;
    }

    /// Gets the skylight at a given point in the world, or fully lit if the world doesn't exist.
    /// @param level *(optional)* The world.
    /// @param lightEmission The old light value for this item, usually the `packedLight` parameter.
    public static float getSkylight(@Nullable Level level, int lightEmission) {
        float skyLight = LightTexture.sky(lightEmission);
        if (level != null && level.isClientSide()) skyLight *= ((ClientLevel) level).getSkyDarken(1);
        return skyLight;
    }
}
