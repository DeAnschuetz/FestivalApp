export class FfbApiError extends Error {
  public readonly status?: number;
  public readonly code?: string;

  constructor(message: string, options?: { status?: number; code?: string }) {
    super(message);
    this.name = "FfbApiError";
    this.status = options?.status;
    this.code = options?.code;
  }
}

type ErrorLikeResponse = {
  status: number;
  data?: {
    code?: string;
    message?: string;
  } | void;
};

export function toApiError(response: ErrorLikeResponse, fallbackMessage: string): FfbApiError {
  const errorData =
    typeof response.data === "object" && response.data !== null ? response.data : undefined;

  return new FfbApiError(errorData?.message ?? fallbackMessage, {
    status: response.status,
    code: errorData?.code,
  });
}

export function isOfflineLikeError(error: unknown): boolean {
  if (error instanceof TypeError) {
    return true;
  }

  if (error instanceof DOMException && error.name === "AbortError") {
    return true;
  }

  if (error instanceof SyntaxError) {
    const message = error.message.toLowerCase();

    if (
      message.includes("unexpected token") ||
      message.includes("not valid json") ||
      message.includes("json")
    ) {
      return true;
    }
  }

  if (error instanceof Error) {
    const message = error.message.toLowerCase();

    if (
      message.includes("proxy error") ||
      message.includes("failed to fetch") ||
      message.includes("networkerror") ||
      message.includes("load failed")
    ) {
      return true;
    }
  }

  return false;
}