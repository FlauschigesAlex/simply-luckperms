package at.flauschigesalex.lucko.loader;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.repository.RepositoryPolicy;

@SuppressWarnings({"UnstableApiUsage", "unused"})
public class SimpleLuckoLoader implements PluginLoader {

    @Override
    public void classloader(PluginClasspathBuilder classpathBuilder) {
        final var resolver = new MavenLibraryResolver();
        resolver.addDependency(new Dependency(new DefaultArtifact("org.jetbrains.kotlin:kotlin-stdlib:2.3.20"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0"), null));
        resolver.addDependency(new Dependency(new DefaultArtifact("at.flauschigesalex.lib.minecraft.paper:minecraft-paper-base:2.0.0"), null));

        final var policy = new RepositoryPolicy(true, RepositoryPolicy.UPDATE_POLICY_ALWAYS, RepositoryPolicy.CHECKSUM_POLICY_FAIL);

        resolver.addRepository(new RemoteRepository.Builder("paper", "default", "https://repo.papermc.io/repository/maven-public/").build());
        resolver.addRepository(new RemoteRepository.Builder("flx-library", "default", "https://repo.flauschigesalex.at/repository/flx-library/")
                .setReleasePolicy(policy)
                .setSnapshotPolicy(policy)
                .build());

        classpathBuilder.addLibrary(resolver);
    }
}
