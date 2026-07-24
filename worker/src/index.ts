const SERVICE_NAME = "ideal-pest-control-api";
const API_VERSION = "1.0.0";

const jsonHeaders = {
  "content-type": "application/json; charset=utf-8",
  "cache-control": "no-store",
  "x-content-type-options": "nosniff",
} as const;

function json(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: jsonHeaders,
  });
}

export default {
  async fetch(request): Promise<Response> {
    const url = new URL(request.url);

    if (url.pathname === "/health" && (request.method === "GET" || request.method === "HEAD")) {
      if (request.method === "HEAD") {
        return new Response(null, { status: 200, headers: jsonHeaders });
      }

      return json({
        status: "ok",
        service: SERVICE_NAME,
        version: API_VERSION,
        timestamp: new Date().toISOString(),
      });
    }

    return json(
      {
        error: "not_found",
        message: "Route not found",
      },
      404,
    );
  },
} satisfies ExportedHandler<Env>;
