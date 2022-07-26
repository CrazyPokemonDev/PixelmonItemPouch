# PixelmonItemPouch
An item pouch side mod for pixelmon generations

For planned features, see [ideas.md](ideas.md).

## Development hints

### Mixins

To make mixins work in the development environment, I had to implement
a rather hacky workaround that replaces the SRG mappings in the annotations
needed to build a valid jar by the MCP mappings needed to run the mod
in the dev environment. If new SRG-mapped mixin annotations are added,
the mappings need to be added to the genDevSources task, because I didn't
bother to figure out how I could apply the entire SRG to MCP mappings
to just these files.
