package ro.unibuc.prodeng.response;

public record MenuResponse(
    String id,
    String name,
    String description,
    Double price,
    boolean available
) {}