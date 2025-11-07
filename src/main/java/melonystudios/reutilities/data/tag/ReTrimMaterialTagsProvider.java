package melonystudios.reutilities.data.tag;

import melonystudios.reutilities.Reutilities;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static melonystudios.reutilities.util.tag.ReTrimMaterialTags.*;

@ApiStatus.Internal
public class ReTrimMaterialTagsProvider extends TagsProvider<TrimMaterial> {
    public ReTrimMaterialTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, @Nullable ExistingFileHelper fileHelper) {
        super(output, Registries.TRIM_MATERIAL, registries, Reutilities.MOD_ID, fileHelper);
    }

    @Override
    @NotNull
    public String getName() {
        return Reutilities.generatorName("Trim Material Tags");
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Common tags
        this.tag(EMISSIVE_LIGHTING);
    }
}
