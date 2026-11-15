package melonystudios.reutilities.client.screen;

import melonystudios.renderslice.api.SliceComponents;
import melonystudios.renderslice.api.component.DropShadowStringWidget;
import melonystudios.renderslice.api.component.NotificationOverlayWidget;
import melonystudios.reutilities.client.screen.list.CommandHelpList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.fml.ModList;

public class CommandHelpScreen extends Screen {
    public static final Component COPIED_TO_CLIPBOARD = Component.translatable("menu.reutilities.command_help.copied_text");
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private CommandHelpList list;
    private NotificationOverlayWidget copyOverlay;
    private final String modID;
    private final String commandID;

    public CommandHelpScreen(String modID, String commandID) {
        super(Component.empty());
        this.modID = modID;
        this.commandID = commandID;
    }

    public String modID() {
        return this.modID;
    }

    public String commandID() {
        return this.commandID;
    }

    @Override
    public Component getTitle() {
        int accentColor = SliceComponents.accentColorOrDefault(this.modID(), 0xFFFFA0);
        String[] category = this.commandID().split("/");
        String modName = getModName(this.modID());

        MutableComponent modNameComponent = Component.translatable("menu.reutilities.command_help.title.mod", modName)
                .withStyle(style -> style.withColor(accentColor).withBold(true));
        MutableComponent separator = Component.translatable("menu.reutilities.command_help.title.separator")
                .withStyle(style -> style.withColor(accentColor).withBold(true));

        if (category.length >= 2) {
            return modNameComponent
                    .append(" ")
                    .append(Component.translatable("menu.reutilities.command_help.category." + category[0]).withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)))
                    .append(separator)
                    .append(Component.translatable("commands.mstudios." + this.commandID().replace('/', '.') + ".title").withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)));
        } else {
            return modNameComponent
                    .append(" ")
                    .append(Component.translatable("commands.mstudios." + this.commandID().replace('/', '.') + ".title").withStyle(style -> style.withColor(ChatFormatting.WHITE).withBold(false)));
        }
    }

    public static String getModName(String modID) {
        return ModList.get().getModContainerById(modID)
                .map(container -> container.getModInfo().getDisplayName())
                .orElse(modID);
    }

    // todo: add a screen that lists all of these commands, with the title text being clickable to navigate the screens
    @Override
    protected void init() {
        this.layout.addToHeader(new DropShadowStringWidget(this.getTitle(), this.font));
        this.list = this.layout.addToContents(new CommandHelpList(this.minecraft, this));
        LinearLayout footerLayout = this.layout.addToFooter(LinearLayout.horizontal()).spacing(8);
        footerLayout.defaultCellSetting().alignHorizontallyCenter();
        footerLayout.addChild(Button.builder(CommonComponents.GUI_DONE, button -> this.onClose()).width(200).build());
        footerLayout.addChild(Button.builder(Component.translatable("menu.reutilities.command_help.shortcuts"), button -> {})
                .width(20)
                .tooltip(Tooltip.create(Component.translatable(
                        "menu.reutilities.command_help.shortcuts.title",
                        Component.translatable("menu.reutilities.command_help.shortcuts.copy" + (Minecraft.ON_OSX ? ".mac" : ""))
                                .withStyle(style -> style.withBold(false).withColor(ChatFormatting.GRAY))
                ).withStyle(style -> style.withBold(true))))
                .build()
        );
        this.layout.arrangeElements();
        this.layout.visitWidgets(this::addRenderableWidget);
        this.copyOverlay = this.addRenderableWidget(new NotificationOverlayWidget(this.width / 2, this.height - 33 - 30, this.list.getRowWidth(), this.font.lineHeight + 3 * 6, COPIED_TO_CLIPBOARD));
    }

    @Override
    protected void repositionElements() {
        this.layout.arrangeElements();
        this.list.updateSize(this.width, this.layout);
    }

    public void resetCopyNotificationTicks() {
        this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK.value(), 1));
        this.copyOverlay.displayNotificationOverlay();
    }
}
