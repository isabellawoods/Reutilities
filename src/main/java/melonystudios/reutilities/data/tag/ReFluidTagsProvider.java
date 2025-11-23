package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static melonystudios.reutilities.util.tag.ReFluidTags.*;

@ApiStatus.Internal
public final class ReFluidTagsProvider extends FluidTagsProvider {
    public ReFluidTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(output, registries, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Fluid Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Reutilities' tags
        this.tag(HYDRATES_WATER_BASED_FARMLAND).addTag(FluidTags.WATER);
        this.tag(HYDRATES_LAVA_BASED_FARMLAND).addTag(FluidTags.LAVA);
    }
}
